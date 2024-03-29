package pl.alekaug.quizplatform.constants;

public abstract class DatabaseConstants {
    /** Questions */
    public static final String QUESTIONS_TABLE = "questions";
    public static final String QUESTION_ID_COLUMN = "q_id";
    public static final String QUESTION_TYPE_COLUMN = "question_type";
    public static final String QUESTION_CONTENT_COLUMN = "question_content";

    /** Answers */
    public static final String ANSWERS_TABLE = "answers";
    public static final String ANSWER_ID_COLUMN = "a_id";
    public static final String ANSWER_CONTENT_COLUMN = "answer_content";
    public static final String ANSWER_IS_CORRECT_COLUMN = "is_correct";

    /** Resources */
    public static final String RESOURCES_TABLE = "resources";
    public static final String RESOURCE_ID_COLUMN = "r_id";
    public static final String RESOURCE_URL_COLUMN = "url";
    public static final String RESOURCE_TYPE_COLUMN = "resource_type";
    public static final String RESOURCE_CAPTION_COLUMN = "caption";

    /** Sessions */
    public static final String SESSIONS_TABLE = "sessions";
    public static final String SESSION_ID_COLUMN = "s_id";
    public static final String SESSION_USERNAME_COLUMN = "username";
    public static final String PASSED_COLUMN = "passed";

    /** Ranking */
    public static final String RANKING_TABLE = "rankings";
    public static final String POINTS_COLUMN = "points";
}
