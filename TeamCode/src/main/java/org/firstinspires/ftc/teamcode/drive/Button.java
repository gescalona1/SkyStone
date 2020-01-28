package org.firstinspires.ftc.teamcode.drive;

import android.os.Build;

import java.util.Objects;

public class Button {
    private int i;
    private BooleanGetter getter;
    private BFunction bFunction;

    public synchronized void press() {
        if(getter.buttonPress()) i++;
        else i = 0;
        if(i == 1) bFunction.function();
    }

    public interface BooleanGetter {
        boolean buttonPress();
    }
    public interface BFunction{
        void function();
    }

    public static class Builder {
        private BooleanGetter getter;
        private BFunction bFunction;

        public Builder() {

        }

        public Builder setbFunction(BFunction bFunction) {
            this.bFunction = bFunction;
            return this;
        }

        public Builder setGetter(BooleanGetter getter) {
            this.getter = getter;
            return this;
        }

        public Button build() {
            Button button = new Button();
            button.bFunction = bFunction;
            button.getter = getter;
            button.i = 0;
            return button;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Button button = (Button) o;
        return getter.equals(button.getter) &&
                bFunction.equals(button.bFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getter, bFunction);
    }
}
