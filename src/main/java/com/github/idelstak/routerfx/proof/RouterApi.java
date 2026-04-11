package com.github.idelstak.routerfx.proof;

public interface RouterApi {
    Challenge fetchChallenge();

    Session login(Credentials credentials, Challenge challenge);

    RadioState fetchRadioState(Session session);
}
