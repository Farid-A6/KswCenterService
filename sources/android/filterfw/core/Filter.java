package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;
import android.filterfw.format.ObjectFormat;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.TextGraphReader;
import android.util.Log;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class Filter {
    static final int STATUS_ERROR = 6;
    static final int STATUS_FINISHED = 5;
    static final int STATUS_PREINIT = 0;
    static final int STATUS_PREPARED = 2;
    static final int STATUS_PROCESSING = 3;
    static final int STATUS_RELEASED = 7;
    static final int STATUS_SLEEPING = 4;
    static final int STATUS_UNPREPARED = 1;
    private static final String TAG = "Filter";
    private long mCurrentTimestamp;
    private HashSet<Frame> mFramesToRelease;
    private HashMap<String, Frame> mFramesToSet;
    private int mInputCount = -1;
    private HashMap<String, InputPort> mInputPorts;
    private boolean mIsOpen = false;
    private boolean mLogVerbose;
    private String mName;
    private int mOutputCount = -1;
    private HashMap<String, OutputPort> mOutputPorts;
    private int mSleepDelay;
    private int mStatus = 0;

    public abstract void process(FilterContext filterContext);

    public abstract void setupPorts();

    @UnsupportedAppUsage
    public Filter(String name) {
        this.mName = name;
        this.mFramesToRelease = new HashSet<>();
        this.mFramesToSet = new HashMap<>();
        this.mStatus = 0;
        this.mLogVerbose = Log.isLoggable(TAG, 2);
    }

    @UnsupportedAppUsage
    public static final boolean isAvailable(String filterName) {
        try {
            try {
                Thread.currentThread().getContextClassLoader().loadClass(filterName).asSubclass(Filter.class);
                return true;
            } catch (ClassCastException e) {
                return false;
            }
        } catch (ClassNotFoundException e2) {
            return false;
        }
    }

    public final void initWithValueMap(KeyValueMap valueMap) {
        initFinalPorts(valueMap);
        initRemainingPorts(valueMap);
        this.mStatus = 1;
    }

    public final void initWithAssignmentString(String assignments) {
        try {
            initWithValueMap(new TextGraphReader().readKeyValueAssignments(assignments));
        } catch (GraphIOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public final void initWithAssignmentList(Object... keyValues) {
        KeyValueMap valueMap = new KeyValueMap();
        valueMap.setKeyValues(keyValues);
        initWithValueMap(valueMap);
    }

    public final void init() throws ProtocolException {
        initWithValueMap(new KeyValueMap());
    }

    public String getFilterClassName() {
        return getClass().getSimpleName();
    }

    public final String getName() {
        return this.mName;
    }

    public boolean isOpen() {
        return this.mIsOpen;
    }

    public void setInputFrame(String inputName, Frame frame) {
        FilterPort port = getInputPort(inputName);
        if (!port.isOpen()) {
            port.open();
        }
        port.setFrame(frame);
    }

    @UnsupportedAppUsage
    public final void setInputValue(String inputName, Object value) {
        setInputFrame(inputName, wrapInputValue(inputName, value));
    }

    /* access modifiers changed from: protected */
    public void prepare(FilterContext context) {
    }

    /* access modifiers changed from: protected */
    public void parametersUpdated(Set<String> set) {
    }

    /* access modifiers changed from: protected */
    public void delayNextProcess(int millisecs) {
        this.mSleepDelay = millisecs;
        this.mStatus = 4;
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return null;
    }

    public final FrameFormat getInputFormat(String portName) {
        return getInputPort(portName).getSourceFormat();
    }

    public void open(FilterContext context) {
    }

    public final int getSleepDelay() {
        return 250;
    }

    public void close(FilterContext context) {
    }

    public void tearDown(FilterContext context) {
    }

    public final int getNumberOfConnectedInputs() {
        int c = 0;
        for (InputPort inputPort : this.mInputPorts.values()) {
            if (inputPort.isConnected()) {
                c++;
            }
        }
        return c;
    }

    public final int getNumberOfConnectedOutputs() {
        int c = 0;
        for (OutputPort outputPort : this.mOutputPorts.values()) {
            if (outputPort.isConnected()) {
                c++;
            }
        }
        return c;
    }

    public final int getNumberOfInputs() {
        if (this.mOutputPorts == null) {
            return 0;
        }
        return this.mInputPorts.size();
    }

    public final int getNumberOfOutputs() {
        if (this.mInputPorts == null) {
            return 0;
        }
        return this.mOutputPorts.size();
    }

    public final InputPort getInputPort(String portName) {
        if (this.mInputPorts != null) {
            InputPort result = this.mInputPorts.get(portName);
            if (result != null) {
                return result;
            }
            throw new IllegalArgumentException("Unknown input port '" + portName + "' on filter " + this + "!");
        }
        throw new NullPointerException("Attempting to access input port '" + portName + "' of " + this + " before Filter has been initialized!");
    }

    public final OutputPort getOutputPort(String portName) {
        if (this.mInputPorts != null) {
            OutputPort result = this.mOutputPorts.get(portName);
            if (result != null) {
                return result;
            }
            throw new IllegalArgumentException("Unknown output port '" + portName + "' on filter " + this + "!");
        }
        throw new NullPointerException("Attempting to access output port '" + portName + "' of " + this + " before Filter has been initialized!");
    }

    /* access modifiers changed from: protected */
    public final void pushOutput(String name, Frame frame) {
        if (frame.getTimestamp() == -2) {
            if (this.mLogVerbose) {
                Log.v(TAG, "Default-setting output Frame timestamp on port " + name + " to " + this.mCurrentTimestamp);
            }
            frame.setTimestamp(this.mCurrentTimestamp);
        }
        getOutputPort(name).pushFrame(frame);
    }

    /* access modifiers changed from: protected */
    public final Frame pullInput(String name) {
        Frame result = getInputPort(name).pullFrame();
        if (this.mCurrentTimestamp == -1) {
            this.mCurrentTimestamp = result.getTimestamp();
            if (this.mLogVerbose) {
                Log.v(TAG, "Default-setting current timestamp from input port " + name + " to " + this.mCurrentTimestamp);
            }
        }
        this.mFramesToRelease.add(result);
        return result;
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
    }

    /* access modifiers changed from: protected */
    public void transferInputPortFrame(String name, FilterContext context) {
        getInputPort(name).transfer(context);
    }

    /* access modifiers changed from: protected */
    public void initProgramInputs(Program program, FilterContext context) {
        if (program != null) {
            for (InputPort inputPort : this.mInputPorts.values()) {
                if (inputPort.getTarget() == program) {
                    inputPort.transfer(context);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void addInputPort(String name) {
        addMaskedInputPort(name, (FrameFormat) null);
    }

    /* access modifiers changed from: protected */
    public void addMaskedInputPort(String name, FrameFormat formatMask) {
        InputPort port = new StreamPort(this, name);
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter " + this + " adding " + port);
        }
        this.mInputPorts.put(name, port);
        port.setPortFormat(formatMask);
    }

    /* access modifiers changed from: protected */
    public void addOutputPort(String name, FrameFormat format) {
        OutputPort port = new OutputPort(this, name);
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter " + this + " adding " + port);
        }
        port.setPortFormat(format);
        this.mOutputPorts.put(name, port);
    }

    /* access modifiers changed from: protected */
    public void addOutputBasedOnInput(String outputName, String inputName) {
        OutputPort port = new OutputPort(this, outputName);
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter " + this + " adding " + port);
        }
        port.setBasePort(getInputPort(inputName));
        this.mOutputPorts.put(outputName, port);
    }

    /* access modifiers changed from: protected */
    public void addFieldPort(String name, Field field, boolean hasDefault, boolean isFinal) {
        InputPort fieldPort;
        field.setAccessible(true);
        if (isFinal) {
            fieldPort = new FinalPort(this, name, field, hasDefault);
        } else {
            fieldPort = new FieldPort(this, name, field, hasDefault);
        }
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter " + this + " adding " + fieldPort);
        }
        fieldPort.setPortFormat(ObjectFormat.fromClass(field.getType(), 1));
        this.mInputPorts.put(name, fieldPort);
    }

    /* access modifiers changed from: protected */
    public void addProgramPort(String name, String varName, Field field, Class varType, boolean hasDefault) {
        field.setAccessible(true);
        InputPort programPort = new ProgramPort(this, name, varName, field, hasDefault);
        if (this.mLogVerbose) {
            Log.v(TAG, "Filter " + this + " adding " + programPort);
        }
        programPort.setPortFormat(ObjectFormat.fromClass(varType, 1));
        this.mInputPorts.put(name, programPort);
    }

    /* access modifiers changed from: protected */
    public void closeOutputPort(String name) {
        getOutputPort(name).close();
    }

    /* access modifiers changed from: protected */
    public void setWaitsOnInputPort(String portName, boolean waits) {
        getInputPort(portName).setBlocking(waits);
    }

    /* access modifiers changed from: protected */
    public void setWaitsOnOutputPort(String portName, boolean waits) {
        getOutputPort(portName).setBlocking(waits);
    }

    public String toString() {
        return "'" + getName() + "' (" + getFilterClassName() + ")";
    }

    /* access modifiers changed from: package-private */
    public final Collection<InputPort> getInputPorts() {
        return this.mInputPorts.values();
    }

    /* access modifiers changed from: package-private */
    public final Collection<OutputPort> getOutputPorts() {
        return this.mOutputPorts.values();
    }

    /* access modifiers changed from: package-private */
    public final synchronized int getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: package-private */
    public final synchronized void unsetStatus(int flag) {
        this.mStatus &= ~flag;
    }

    /* access modifiers changed from: package-private */
    public final synchronized void performOpen(FilterContext context) {
        if (!this.mIsOpen) {
            if (this.mStatus == 1) {
                if (this.mLogVerbose) {
                    Log.v(TAG, "Preparing " + this);
                }
                prepare(context);
                this.mStatus = 2;
            }
            if (this.mStatus == 2) {
                if (this.mLogVerbose) {
                    Log.v(TAG, "Opening " + this);
                }
                open(context);
                this.mStatus = 3;
            }
            if (this.mStatus == 3) {
                this.mIsOpen = true;
            } else {
                throw new RuntimeException("Filter " + this + " was brought into invalid state during opening (state: " + this.mStatus + ")!");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void performProcess(FilterContext context) {
        if (this.mStatus != 7) {
            transferInputFrames(context);
            if (this.mStatus < 3) {
                performOpen(context);
            }
            if (this.mLogVerbose) {
                Log.v(TAG, "Processing " + this);
            }
            this.mCurrentTimestamp = -1;
            process(context);
            releasePulledFrames(context);
            if (filterMustClose()) {
                performClose(context);
            }
        } else {
            throw new RuntimeException("Filter " + this + " is already torn down!");
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void performClose(FilterContext context) {
        if (this.mIsOpen) {
            if (this.mLogVerbose) {
                Log.v(TAG, "Closing " + this);
            }
            this.mIsOpen = false;
            this.mStatus = 2;
            close(context);
            closePorts();
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void performTearDown(FilterContext context) {
        performClose(context);
        if (this.mStatus != 7) {
            tearDown(context);
            this.mStatus = 7;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003f, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean canProcess() {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.mLogVerbose     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x002a
            java.lang.String r0 = "Filter"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0042 }
            r1.<init>()     // Catch:{ all -> 0x0042 }
            java.lang.String r2 = "Checking if can process: "
            r1.append(r2)     // Catch:{ all -> 0x0042 }
            r1.append(r3)     // Catch:{ all -> 0x0042 }
            java.lang.String r2 = " ("
            r1.append(r2)     // Catch:{ all -> 0x0042 }
            int r2 = r3.mStatus     // Catch:{ all -> 0x0042 }
            r1.append(r2)     // Catch:{ all -> 0x0042 }
            java.lang.String r2 = ")."
            r1.append(r2)     // Catch:{ all -> 0x0042 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0042 }
            android.util.Log.v(r0, r1)     // Catch:{ all -> 0x0042 }
        L_0x002a:
            int r0 = r3.mStatus     // Catch:{ all -> 0x0042 }
            r1 = 3
            r2 = 0
            if (r0 > r1) goto L_0x0040
            boolean r0 = r3.inputConditionsMet()     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x003e
            boolean r0 = r3.outputConditionsMet()     // Catch:{ all -> 0x0042 }
            if (r0 == 0) goto L_0x003e
            r2 = 1
        L_0x003e:
            monitor-exit(r3)
            return r2
        L_0x0040:
            monitor-exit(r3)
            return r2
        L_0x0042:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.filterfw.core.Filter.canProcess():boolean");
    }

    /* access modifiers changed from: package-private */
    public final void openOutputs() {
        if (this.mLogVerbose) {
            Log.v(TAG, "Opening all output ports on " + this + "!");
        }
        for (OutputPort outputPort : this.mOutputPorts.values()) {
            if (!outputPort.isOpen()) {
                outputPort.open();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void clearInputs() {
        for (InputPort inputPort : this.mInputPorts.values()) {
            inputPort.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public final void clearOutputs() {
        for (OutputPort outputPort : this.mOutputPorts.values()) {
            outputPort.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public final void notifyFieldPortValueUpdated(String name, FilterContext context) {
        if (this.mStatus == 3 || this.mStatus == 2) {
            fieldPortValueUpdated(name, context);
        }
    }

    /* access modifiers changed from: package-private */
    public final synchronized void pushInputFrame(String inputName, Frame frame) {
        FilterPort port = getInputPort(inputName);
        if (!port.isOpen()) {
            port.open();
        }
        port.pushFrame(frame);
    }

    /* access modifiers changed from: package-private */
    public final synchronized void pushInputValue(String inputName, Object value) {
        pushInputFrame(inputName, wrapInputValue(inputName, value));
    }

    private final void initFinalPorts(KeyValueMap values) {
        this.mInputPorts = new HashMap<>();
        this.mOutputPorts = new HashMap<>();
        addAndSetFinalPorts(values);
    }

    private final void initRemainingPorts(KeyValueMap values) {
        addAnnotatedPorts();
        setupPorts();
        setInitialInputValues(values);
    }

    private final void addAndSetFinalPorts(KeyValueMap values) {
        for (Field field : getClass().getDeclaredFields()) {
            Annotation annotation = field.getAnnotation(GenerateFinalPort.class);
            Annotation annotation2 = annotation;
            if (annotation != null) {
                GenerateFinalPort generator = (GenerateFinalPort) annotation2;
                String name = generator.name().isEmpty() ? field.getName() : generator.name();
                addFieldPort(name, field, generator.hasDefault(), true);
                if (values.containsKey(name)) {
                    setImmediateInputValue(name, values.get(name));
                    values.remove(name);
                } else if (!generator.hasDefault()) {
                    throw new RuntimeException("No value specified for final input port '" + name + "' of filter " + this + "!");
                }
            }
        }
    }

    private final void addAnnotatedPorts() {
        for (Field field : getClass().getDeclaredFields()) {
            Annotation annotation = field.getAnnotation(GenerateFieldPort.class);
            Annotation annotation2 = annotation;
            if (annotation != null) {
                addFieldGenerator((GenerateFieldPort) annotation2, field);
            } else {
                Annotation annotation3 = field.getAnnotation(GenerateProgramPort.class);
                Annotation annotation4 = annotation3;
                if (annotation3 != null) {
                    addProgramGenerator((GenerateProgramPort) annotation4, field);
                } else {
                    Annotation annotation5 = field.getAnnotation(GenerateProgramPorts.class);
                    Annotation annotation6 = annotation5;
                    if (annotation5 != null) {
                        for (GenerateProgramPort generator : ((GenerateProgramPorts) annotation6).value()) {
                            addProgramGenerator(generator, field);
                        }
                    }
                }
            }
        }
    }

    private final void addFieldGenerator(GenerateFieldPort generator, Field field) {
        addFieldPort(generator.name().isEmpty() ? field.getName() : generator.name(), field, generator.hasDefault(), false);
    }

    private final void addProgramGenerator(GenerateProgramPort generator, Field field) {
        String name = generator.name();
        addProgramPort(name, generator.variableName().isEmpty() ? name : generator.variableName(), field, generator.type(), generator.hasDefault());
    }

    private final void setInitialInputValues(KeyValueMap values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            setInputValue(entry.getKey(), entry.getValue());
        }
    }

    private final void setImmediateInputValue(String name, Object value) {
        if (this.mLogVerbose) {
            Log.v(TAG, "Setting immediate value " + value + " for port " + name + "!");
        }
        FilterPort port = getInputPort(name);
        port.open();
        port.setFrame(SimpleFrame.wrapObject(value, (FrameManager) null));
    }

    private final void transferInputFrames(FilterContext context) {
        for (InputPort inputPort : this.mInputPorts.values()) {
            inputPort.transfer(context);
        }
    }

    private final Frame wrapInputValue(String inputName, Object value) {
        Frame frame;
        boolean shouldSerialize = true;
        MutableFrameFormat inputFormat = ObjectFormat.fromObject(value, 1);
        if (value == null) {
            FrameFormat portFormat = getInputPort(inputName).getPortFormat();
            inputFormat.setObjectClass(portFormat == null ? null : portFormat.getObjectClass());
        }
        if ((value instanceof Number) || (value instanceof Boolean) || (value instanceof String) || !(value instanceof Serializable)) {
            shouldSerialize = false;
        }
        if (shouldSerialize) {
            frame = new SerializedFrame(inputFormat, (FrameManager) null);
        } else {
            frame = new SimpleFrame(inputFormat, (FrameManager) null);
        }
        Frame frame2 = frame;
        frame2.setObjectValue(value);
        return frame2;
    }

    private final void releasePulledFrames(FilterContext context) {
        Iterator<Frame> it = this.mFramesToRelease.iterator();
        while (it.hasNext()) {
            context.getFrameManager().releaseFrame(it.next());
        }
        this.mFramesToRelease.clear();
    }

    private final boolean inputConditionsMet() {
        for (FilterPort port : this.mInputPorts.values()) {
            if (!port.isReady()) {
                if (!this.mLogVerbose) {
                    return false;
                }
                Log.v(TAG, "Input condition not met: " + port + "!");
                return false;
            }
        }
        return true;
    }

    private final boolean outputConditionsMet() {
        for (FilterPort port : this.mOutputPorts.values()) {
            if (!port.isReady()) {
                if (!this.mLogVerbose) {
                    return false;
                }
                Log.v(TAG, "Output condition not met: " + port + "!");
                return false;
            }
        }
        return true;
    }

    private final void closePorts() {
        if (this.mLogVerbose) {
            Log.v(TAG, "Closing all ports on " + this + "!");
        }
        for (InputPort inputPort : this.mInputPorts.values()) {
            inputPort.close();
        }
        for (OutputPort outputPort : this.mOutputPorts.values()) {
            outputPort.close();
        }
    }

    private final boolean filterMustClose() {
        for (InputPort inputPort : this.mInputPorts.values()) {
            if (inputPort.filterMustClose()) {
                if (this.mLogVerbose) {
                    Log.v(TAG, "Filter " + this + " must close due to port " + inputPort);
                }
                return true;
            }
        }
        for (OutputPort outputPort : this.mOutputPorts.values()) {
            if (outputPort.filterMustClose()) {
                if (this.mLogVerbose) {
                    Log.v(TAG, "Filter " + this + " must close due to port " + outputPort);
                }
                return true;
            }
        }
        return false;
    }
}
