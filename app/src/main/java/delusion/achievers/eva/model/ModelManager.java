package delusion.achievers.eva.model;


/*
 * Created by arun on 16/12/15.
 */
public class ModelManager {

    private static ModelManager modelMgr = null;

    private BeaconsManager beaconsManager;

    private ModelManager() {
        beaconsManager = new BeaconsManager();
    }

    public void clearManagerInstance() {
        this.beaconsManager = null;
    }

    public static ModelManager getInstance() {
        if (modelMgr == null) {
            modelMgr = new ModelManager();
        }
        return modelMgr;
    }

    public static void setInstance() {
        modelMgr = new ModelManager();
    }

    public static boolean getInstanceModelManager() {
        return modelMgr != null;
    }

    public BeaconsManager getBeaconsManager() {
        return beaconsManager;
    }
}
