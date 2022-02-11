package zmq.com.ystlibrary.model;

import java.util.ArrayList;
import java.util.List;

/**
 * updated by ZMQ501 on Feb/4/2022.
 */

public class PreAndPostResult {
    public PreTestScore PreTestScore;
    public PostTestScore PostTestScore;
    public List<Integer> PreTestQstID;
    public List<Integer> PostTestQstID;
    public List<String> PreTestAnswer;
    public List<String> PostTestAnswer;
    public List<String> PreTestOptions;
    public List<String> PostTestOptions;
    public List<Integer> PreTestQuestinwiseScores;
    public List<Integer> PostTestQuestinwiseScores;

 /*   public PreAndPostResult() {
        PreTestScore = new PreTestScore();
        PostTestScore = new PostTestScore();
        PreTestQstID = new ArrayList<>();
        PostTestQstID = new ArrayList<>();
        PreTestAnswer = new ArrayList<>();
        PostTestAnswer = new ArrayList<>();
        PreTestOptions = new ArrayList<>();
        PostTestOptions = new ArrayList<>();
    }*/

    public void setPreTestScore(PreTestScore preTestScore) {
        PreTestScore = preTestScore;
    }

    public void setPostTestScore(PostTestScore postTestScore) {
        PostTestScore = postTestScore;
    }

    public void setPreTestQstID(List<Integer> preTestQstID) {
        PreTestQstID = preTestQstID;
    }

    public void setPostTestQstID(List<Integer> postTestQstID) {
        PostTestQstID = postTestQstID;
    }

    public void setPreTestAnswer(List<String> preTestAnswer) {
        PreTestAnswer = preTestAnswer;
    }

    public void setPostTestAnswer(List<String> postTestAnswer) {
        PostTestAnswer = postTestAnswer;
    }

    public void setPreTestOptions(List<String> preTestOptions) {
        PreTestOptions = preTestOptions;
    }

    public void setPostTestOptions(List<String> postTestOptions) {
        PostTestOptions = postTestOptions;
    }

    public void setPreTestQuestinwiseScores(List<Integer >preTestQuestinwiseScores)
    {
        PreTestQuestinwiseScores=preTestQuestinwiseScores;
    }
    public void setPostTestQuestinwiseScores(List<Integer>postTestQuestinwiseScores)
    {
        PostTestQuestinwiseScores=postTestQuestinwiseScores;
    }
    public static class PreTestScore{
        public int PreTestID;
        public int MiraID;
        public int GroupID;
        public int PregWomenID;
        public int NonPregWomenID;
        public String PregWomenName;
        public String NonPregWomenName;
        public int StoryID;
        public double Score;
        public String PreStartDate;
        public String PreStartTime;
        public String PreCloseTime;
        public int PreClose;

    }

    public static class PostTestScore{
        public int PostTestID;
        public int MiraID;
        public int GroupID;
        public int PregWomenID;
        public int NonPregWomenID;
        public String PregWomenName;
        public String NonPregWomenName;
        public int StoryID;
        public double Score;
        public String PostStartDate;
        public String PostStartTime;
        public String PostCloseTime;
        public int PostClose;

    }
    public int ExternalMiraID;
    public int ExternalUserID;
}
