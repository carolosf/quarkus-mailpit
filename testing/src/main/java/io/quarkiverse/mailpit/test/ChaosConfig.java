package io.quarkiverse.mailpit.test;

import io.quarkiverse.mailpit.test.model.Trigger;
import io.quarkiverse.mailpit.test.model.Triggers;

public class ChaosConfig {
    private final Trigger authentication;
    private final Trigger recipient;
    private final Trigger sender;

    private ChaosConfig(Builder builder) {
        this.authentication = builder.authentication;
        this.recipient = builder.recipient;
        this.sender = builder.sender;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Trigger authentication;
        private Trigger recipient;
        private Trigger sender;

        public Builder authentication(int errorCode, int probability) {
            this.authentication = getNewTrigger(errorCode, probability);
            return this;
        }

        public Builder recipient(int errorCode, int probability) {
            this.recipient = getNewTrigger(errorCode, probability);
            return this;
        }

        public Builder sender(int errorCode, int probability) {
            this.sender = getNewTrigger(errorCode, probability);
            return this;
        }

        private Trigger getNewTrigger(int errorCode, int probability) {
            assertErrorCode(errorCode);
            assertProbability(probability);

            Trigger trigger = new Trigger();
            trigger.setErrorCode((long) errorCode);
            trigger.setProbability((long) probability);

            return trigger;
        }

        private static void assertProbability(int probability) {
            if (probability < 0 || probability > 100) {
                throw new IllegalArgumentException("Probability must be between 0 and 100.");
            }
        }

        private static void assertErrorCode(int errorCode) {
            if (errorCode < 400 || errorCode > 599) {
                throw new IllegalArgumentException("ErrorCode must be a valid SMTP error code (400-599).");
            }
        }

        public ChaosConfig build() {
            // TODO: could make it less verbose and set up defaults as 0 probability
            if (authentication == null || recipient == null || sender == null) {
                throw new IllegalStateException("All fields (Authentication, Recipient, Sender) must be set.");
            }
            return new ChaosConfig(this);
        }
    }

    public Triggers getTriggers() {
        Triggers triggers = new Triggers();
        triggers.setAuthentication(authentication);
        triggers.setRecipient(recipient);
        triggers.setSender(sender);

        //TODO: We should probably not use Triggers and make our own object so that we can protect ourselves from swagger API changes

        return triggers;
    }

    @Override
    public String toString() {
        return "ChaosConfig{" +
                "authentication=" + authentication +
                ", recipient=" + recipient +
                ", sender=" + sender +
                '}';
    }
}
