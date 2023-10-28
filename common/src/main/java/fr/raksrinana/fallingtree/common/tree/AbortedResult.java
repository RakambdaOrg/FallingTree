package fr.raksrinana.fallingtree.common.tree;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AbortedResult implements IBreakAttemptResult {
    INVALID_PLAYER_STATE(false),
    NOT_ENABLED(false),
    NOT_SERVER(false),
    NO_SUCH_TREE(false),
    REQUIRED_TOOL_ABSENT(true),
    TREE_TOO_BIG_BREAK(false),
    TREE_TOO_BIG_SCAN(false);

    private final boolean cancel;

    @Override
    public boolean shouldCancel() {
        return cancel;
    }
}
