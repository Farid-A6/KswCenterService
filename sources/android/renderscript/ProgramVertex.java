package android.renderscript;

import android.annotation.UnsupportedAppUsage;
import android.renderscript.Program;

public class ProgramVertex extends Program {
    ProgramVertex(long id, RenderScript rs) {
        super(id, rs);
    }

    public int getInputCount() {
        if (this.mInputs != null) {
            return this.mInputs.length;
        }
        return 0;
    }

    public Element getInput(int slot) {
        if (slot >= 0 && slot < this.mInputs.length) {
            return this.mInputs[slot];
        }
        throw new IllegalArgumentException("Slot ID out of range.");
    }

    public static class Builder extends Program.BaseProgramBuilder {
        @UnsupportedAppUsage
        public Builder(RenderScript rs) {
            super(rs);
        }

        @UnsupportedAppUsage
        public Builder addInput(Element e) throws IllegalStateException {
            if (this.mInputCount >= 8) {
                throw new RSIllegalArgumentException("Max input count exceeded.");
            } else if (!e.isComplex()) {
                Element[] elementArr = this.mInputs;
                int i = this.mInputCount;
                this.mInputCount = i + 1;
                elementArr[i] = e;
                return this;
            } else {
                throw new RSIllegalArgumentException("Complex elements not allowed.");
            }
        }

        @UnsupportedAppUsage
        public ProgramVertex create() {
            this.mRS.validate();
            long[] tmp = new long[((this.mInputCount + this.mOutputCount + this.mConstantCount + this.mTextureCount) * 2)];
            String[] texNames = new String[this.mTextureCount];
            int i = 0;
            int idx = 0;
            for (int i2 = 0; i2 < this.mInputCount; i2++) {
                int idx2 = idx + 1;
                tmp[idx] = (long) Program.ProgramParam.INPUT.mID;
                idx = idx2 + 1;
                tmp[idx2] = this.mInputs[i2].getID(this.mRS);
            }
            for (int i3 = 0; i3 < this.mOutputCount; i3++) {
                int idx3 = idx + 1;
                tmp[idx] = (long) Program.ProgramParam.OUTPUT.mID;
                idx = idx3 + 1;
                tmp[idx3] = this.mOutputs[i3].getID(this.mRS);
            }
            for (int i4 = 0; i4 < this.mConstantCount; i4++) {
                int idx4 = idx + 1;
                tmp[idx] = (long) Program.ProgramParam.CONSTANT.mID;
                idx = idx4 + 1;
                tmp[idx4] = this.mConstants[i4].getID(this.mRS);
            }
            while (true) {
                int i5 = i;
                if (i5 < this.mTextureCount) {
                    int idx5 = idx + 1;
                    tmp[idx] = (long) Program.ProgramParam.TEXTURE_TYPE.mID;
                    idx = idx5 + 1;
                    tmp[idx5] = (long) this.mTextureTypes[i5].mID;
                    texNames[i5] = this.mTextureNames[i5];
                    i = i5 + 1;
                } else {
                    ProgramVertex pv = new ProgramVertex(this.mRS.nProgramVertexCreate(this.mShader, texNames, tmp), this.mRS);
                    initProgram(pv);
                    return pv;
                }
            }
        }
    }
}
