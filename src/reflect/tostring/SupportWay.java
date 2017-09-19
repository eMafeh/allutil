package reflect.tostring;

/**
 * Created by QianRui on 2017/9/11.
 */
enum SupportWay {
    XML(DetailBuild::xmlstring, DetailBuild::xmlbefore, DetailBuild::xmlafter),
    KEYVALUE(DetailBuild::kvstring, DetailBuild::donot, DetailBuild::donot);


    private final KeyValuable goString;
    private final KeyValuable before;
    private final KeyValuable after;

    SupportWay(KeyValuable goString, KeyValuable before, KeyValuable after) {
        this.goString = goString;
        this.before = before;
        this.after = after;
    }

    public KeyValuable getGoString() {
        return goString;
    }

    public KeyValuable getBefore() {
        return before;
    }

    public KeyValuable getAfter() {
        return after;
    }


}
