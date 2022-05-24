package jp.haru_idea.springboot.ec_site.models;

public enum DiscontinuedFlag {
    CONTINUED(0,"―"),DISCONTINUED(1,"廃版");
    private int flag;
    private String flagStr;

    private DiscontinuedFlag(int flag, String flagStr){
        this.flag = flag;
        this.flagStr = flagStr;
    }

    public int getFlag() {
        return flag;
    }

    public String getFlagStr() {
        return flagStr;
    }

}
