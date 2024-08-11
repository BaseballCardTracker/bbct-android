package bbct.android.data

enum class BaseballCardSchemaVersion(val version: Int) {
    ORIGINAL_SCHEMA(1),
    BAD_TEAM_SCHEMA(2),
    TEAM_SCHEMA(3),
    AUTO_AND_CONDITION_SCHEMA(4),
    ROOM_SCHEMA(5),
    ALPHA_NUMERIC_SCHEMA(6),
}
