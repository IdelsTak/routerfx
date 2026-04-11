package com.github.idelstak.routerfx.router.protocol;

import java.util.*;

public sealed interface RouterFault {

    String detail();

    record AuthFault(String detail) implements RouterFault {

        public AuthFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record SessionExpiredFault(String detail) implements RouterFault {

        public SessionExpiredFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record TimeoutFault(String detail) implements RouterFault {

        public TimeoutFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record TransportFault(String detail) implements RouterFault {

        public TransportFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record ProtocolFault(String detail) implements RouterFault {

        public ProtocolFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record MalformedResponseFault(String detail) implements RouterFault {

        public MalformedResponseFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }

    record UnsupportedCommandFault(String detail) implements RouterFault {

        public UnsupportedCommandFault {
            Objects.requireNonNull(detail, "detail must not be null");
        }
    }
}
