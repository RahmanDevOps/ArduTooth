package zmq.com.ystlibrary.model;

import java.io.Serializable;
import java.util.List;

/**
 * updated by ZMQ501 on Feb/4/2022.
 */

public class PreAndPost implements Serializable {
    public PreTestDetails PreTestDetails;
    public PostTestDetails PostTestDetails;
    public class PreTestDetails implements Serializable{
        public int PreTestID;
        public int TotalQuestion;
        public int EachQuestionMark;
        public String PreTestTitleE;
        public String PreTestTitleH;
        public String PreTestUrl;
        public List<Question> PreTestQuestionH;
        public List<Question> PreTestQuestionE;
    }
    public class PostTestDetails implements Serializable{
        public int PostTestID;
        public int TotalQuestion;
        public int EachQuestionMark;
        public String PostTestTitleE;
        public String PostTestTitleH;
        public String PostTestUrl;
        public List<Question> PostTestQuestionH;
        public List<Question> PostTestQuestionE;

    }

    public class Question implements Serializable{
        public int QuestionID;
        public String QuestionText;
        public String OptA;
        public String OptB;
        public String OptC;
        public String OptD;
        public String RightAnswer;
        public String RightOption;
    }


}
