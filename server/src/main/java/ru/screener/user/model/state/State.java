package ru.screener.user.model.state;

public enum State {

    NONE(InputKind.NONE),
    WAIT_TIME_FRAME_SHORT(InputKind.TIME_FRAME),
    WAIT_TIME_FRAME_LONG(InputKind.TIME_FRAME),
    WAIT_RSI_SHORT(InputKind.RSI),
    WAIT_RSI_LONG(InputKind.RSI);

    final InputKind kind;

    State(InputKind kind) {
        this.kind = kind;
    }
}
