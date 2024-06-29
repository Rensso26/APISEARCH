package model;

import java.time.LocalDate;
import java.util.List;

public class Photos {
    private int id;
    private int sol;
    private String cm_name;
    private String cm_full_name;
    private String img_src;
    private LocalDate earth_date;
    private String rover_name;
    private String rover_status;
    private LocalDate landing_date;
    private LocalDate launch_date;
    private int max_sol;
    private LocalDate max_date;
    private int total_photos;
    private List<String> cameras;

    public Photos(int id, int sol, String cm_name, String cm_full_name, String img_src, LocalDate earth_date, String rover_name, String rover_status, LocalDate landing_date, LocalDate launch_date, int max_sol, LocalDate max_date, int total_photos, List<String> cameras) {
        this.id = id;
        this.sol = sol;
        this.cm_name = cm_name;
        this.cm_full_name = cm_full_name;
        this.img_src = img_src;
        this.earth_date = earth_date;
        this.rover_name = rover_name;
        this.rover_status = rover_status;
        this.landing_date = landing_date;
        this.launch_date = launch_date;
        this.max_sol = max_sol;
        this.max_date = max_date;
        this.total_photos = total_photos;
        this.cameras = cameras;
    }
    
    public int getId() {
        return id;
    }

    public int getSol() {
        return sol;
    }

    public String getCm_name() {
        return cm_name;
    }

    public String getCm_full_name() {
        return cm_full_name;
    }

    public String getImg_src() {
        return img_src;
    }

    public LocalDate getEarth_date() {
        return earth_date;
    }

    public String getRover_name() {
        return rover_name;
    }

    public String getRover_status() {
        return rover_status;
    }

    public LocalDate getLanding_date() {
        return landing_date;
    }

    public LocalDate getLaunch_date() {
        return launch_date;
    }

    public int getMax_sol() {
        return max_sol;
    }

    public LocalDate getMax_date() {
        return max_date;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public List<String> getCameras() {
        return cameras;
    }

    @Override
    public String toString() {
        return "MarsPhoto{" +
                "id=" + id +
                ", sol=" + sol +
                ", camera_name='" + cm_name + '\'' +
                ", camera_full_name='" + cm_full_name + '\'' +
                ", img_src='" + img_src + '\'' +
                ", earth_date=" + earth_date +
                ", rover_name='" + rover_name + '\'' +
                ", rover_status='" + rover_status + '\'' +
                ", rover_landing_date=" + landing_date +
                ", rover_launch_date=" + launch_date +
                ", rover_max_sol=" + max_sol +
                ", rover_max_date=" + max_date +
                ", rover_total_photos=" + total_photos +
                ", rover_cameras=" + cameras +
                '}';
    }
    enum RoverStatus {
        ACTIVE,
        INACTIVE,
        UNKNOWN
    }
}
