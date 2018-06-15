/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.dbcls.lodsurfer.psurfer;

//import javax.json.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;

/**
 *
 * @author atsuko
 */
public class MPCtrl {
    Map<String, List<ClassPath>> c2paths = null;
    Set<String> epc = null; // endpoints having classes
    Map<String, CInfo> cinfo = null;

    public void init(){
        List<ClassPath> paths = MPIO.readPaths("paths.json");
        c2paths = getC2Paths(paths);
        epc = MPIO.readEPs("ep.txt"); // kore
        cinfo = MPIO.readCInfo("cinfo.txt"); // koremo
    }
    
    public String getClasses(){
        return getClasses("");
    }
    
    public String getClasses(String classURI){
        if (c2paths == null ){
            init();
        }
        Set<String> cls = c2paths.keySet();
        Set<String> cls2 = null;
        if (classURI.length() == 0){
            cls2 = cls;
        }else{
            cls2 = new HashSet<>();
            Iterator<String> cit = cls.iterator(); 
            while(cit.hasNext()){ 
                String last = cit.next();
                List<ClassPath> pl = c2paths.get(last);
                ListIterator<ClassPath> pit = pl.listIterator();
                while ( pit.hasNext() ){
                    ClassPath cp = pit.next();
                    if (cp.classes.get(0).equals(classURI)){
                        cls2.add(last);
                        break;
                    }
                }
            }
        }
        // 
        List<CInfo> cinf = new LinkedList<>();
        Iterator<String> cit = cls2.iterator();
        while( cit.hasNext() ){
            String cl = cit.next();
            cinf.add(cinfo.get(cl));
        }
        
        return toJson(cinf);
    }

    public String getResult(String filename, String classURI){
        MPData mpd = MPIO.toMPData(filename);
        return toJson(getResult(mpd, classURI));
    }
    
    public MPOutput getResult(MPData mpd, String classURI){
        ClassPath cp = c2paths.get(classURI).get(0);
        return MPQuery.getResult(mpd, cp, epc, cinfo);
    }

    public String getResultFromClasses(String filename, List<String> classURIs ){
        MPData mpd = MPIO.toMPData(filename);
        MPOutput mpo = new MPOutput();
        ListIterator<String> cit = classURIs.listIterator();
        while ( cit.hasNext()){
            String cl = cit.next();
            MPQuery.addResult(mpd, c2paths.get(cl).get(0), epc, cinfo, mpo);
        }
        return toJson(mpo.all);
    }

    private Map<String, List<ClassPath>> getC2Paths(List<ClassPath> paths){
        Map<String, List<ClassPath>> c2p = new HashMap<>();
        ListIterator<ClassPath> pit = paths.listIterator();
        while ( pit.hasNext() ){
            ClassPath cp = pit.next();
            String last = cp.classes.get(cp.classes.size()-1);
            List<ClassPath> cpl = c2p.get(last);
            if ( cpl == null ){
                cpl = new LinkedList<>(); 
            }
            cpl.add(cp);
            c2p.put(last, cpl);
        }
        return c2p;
    }
    
    public static String toJson(Object o){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonstr = "";
        try{
            jsonstr = mapper.writeValueAsString(o);
        }catch(IOException e){
            e.printStackTrace();
        }
        return jsonstr;
    }     

}
