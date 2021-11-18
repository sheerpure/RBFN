package hw2;


import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class drawtest extends JPanel{
    float [][] testdata = new float[3000][3];

    int testtime = 0;
    public drawtest(float data[][], int time){
        testtime = time;
        for (int i = 0; i < testtime; i++) {
            for (int j = 0; j < 3; j++) {
                testdata[i][j] = data[i][j];
            }
        }
        setLayout(new BorderLayout());
        GraphicsConfiguration gc= SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(gc);
        //canvas3D.setLocation(0,0);
        canvas3D.setSize(700,300);


        BranchGroup scene = createSceneGraph();
        scene.compile();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);


        // This moves the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);

        add("Center",canvas3D);

    }

    public BranchGroup createSceneGraph() {
        BranchGroup lineGroup = new BranchGroup();
        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(new Color3f(204.0f, 204.0f,          204.0f), ColoringAttributes.SHADE_FLAT);
        app.setColoringAttributes(ca);

        Point3f[] plaPts = new Point3f[testtime*2];
        Color3f[] colPts = new Color3f[testtime*2];

        for(int i=0;i<testtime;i++){
            int k = 0;
            plaPts[i] = new Point3f(testdata[i][k],testdata[i][k+1],testdata[i][k+2]);
            colPts[i] = new Color3f(1.0f ,1.0f, 1.0f);
        }
        String line,tempstring;
        String[] temparray = new String[2];
        try {
            FileReader fr = new FileReader("test2.txt");
            BufferedReader br = new BufferedReader(fr);
            int tt = 0;
            while ((line = br.readLine()) != null) {
                tempstring = line;
                temparray = tempstring.split("\\s");
                int index = 0;
                plaPts[testtime+tt] = new Point3f(Float.parseFloat(temparray[index]),Float.parseFloat(temparray[index+1]),Float.parseFloat(temparray[index+2]));
                colPts[testtime+tt] = new Color3f(1.0f,0.0f,0.0f);
                tt++;
            }
        }
        catch (IOException e) {System.out.println(e);}
        PointArray pla = new PointArray(testtime*2, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        pla.setColors(0,colPts);
        pla.setCoordinates(0, plaPts);
        //between here!
        PointAttributes a_point_just_bigger=new PointAttributes();
        a_point_just_bigger.setPointSize(10.0f);//10 pixel-wide point
        a_point_just_bigger.setPointAntialiasingEnable(true);//now points are sphere-like(not a cube)
        app.setPointAttributes(a_point_just_bigger);
        //and here! sets the point-attributes so it is easily seen.
        Shape3D plShape = new Shape3D(pla, app);
        TransformGroup objRotate = new TransformGroup();
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRotate.addChild(plShape);

        lineGroup.addChild(objRotate);

        MouseRotate f1=new MouseRotate();
        f1.setSchedulingBounds(new BoundingSphere());
        f1.setTransformGroup(objRotate);
        lineGroup.addChild(f1);

        MouseZoom mz=new MouseZoom();
        mz.setTransformGroup(objRotate);
        mz.setSchedulingBounds(new BoundingSphere());
        lineGroup.addChild(mz);

        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(objRotate);
        mouseTranslate.setSchedulingBounds(new BoundingSphere());
        lineGroup.addChild(mouseTranslate);

        return lineGroup;
    }




}
