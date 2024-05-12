package ru.practicum.explorewithme.basic.lib.dto.events.enums;

import lombok.Getter;

@Getter
public enum StateAction {
    SEND_TO_REVIEW(EventState.PENDING),
    CANCEL_REVIEW(EventState.CANCELED),
    PUBLISH_EVENT(EventState.PUBLISHED),
    REJECT_EVENT(EventState.CANCELED);

    private final EventState nextState;

    StateAction(EventState nextState) {
        this.nextState = nextState;
    }
}
