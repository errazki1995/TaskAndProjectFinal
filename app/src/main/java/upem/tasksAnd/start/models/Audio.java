package upem.tasksAnd.start.models;

public class Audio {
    private int audioid;
    private String audiopath;
    private String audioname;

    public Audio(){

    }

    public Audio(int audioid,String path,String name){
     this.audioid=audioid;
     this.audiopath=path;
     this.audioname=name;
    }
    public int getAudioid() {
        return audioid;
    }

    public void setAudio(int audioid) {
        this.audioid = audioid;
    }

    public String getAudiopath() {
        return audiopath;
    }

    public void setAudiopath(String audiopath) {
        this.audiopath = audiopath;
    }

    public String getAudioname() {
        return audioname;
    }

    public void setaudioname(String audioname) {
        this.audioname = audioname;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "audioid=" + audioid +
                ", audiopath='" + audiopath + '\'' +
                ", audioname='" + audioname + '\'' +
                '}';
    }
}
