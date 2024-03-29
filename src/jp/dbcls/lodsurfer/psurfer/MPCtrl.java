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
    Map<String, ClassPath> n2path = null;
    Set<String> epc = null; // endpoints having classes
    Map<String, CInfo> cinfo = null;

    public void init(){
        //List<ClassPath> paths = MPIO.readPaths("paths.json");
        n2path = MPIO.readNPaths("paths.json");
        //c2paths = getC2Paths(paths);
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

    /*
    public String getResult(String filename, String classURI){
        MPData mpd = MPIO.toMPData(filename);
        return toJson(getResult(mpd, classURI));
    }
    
    public MPOutput getResult(MPData mpd, String classURI){
        ClassPath cp = c2paths.get(classURI).get(0);
        return MPQuery.getResult(mpd, cp, epc, cinfo);
    }
    */

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

    public String getResultFromPNames(MPData mpd, List<String> pns ){
        MPOutput mpo = new MPOutput();
        ListIterator<String> pit = pns.listIterator();
        while ( pit.hasNext()){
            String pn = pit.next();
            MPQuery.addResult(mpd, n2path.get(pn), epc, cinfo, mpo);
        }
        return toJson(mpo.all);
    }    
    
    public String getResultFromPNames(String filename, List<String> pns ){
        MPData mpd = MPIO.toMPData(filename);
        return getResultFromPNames(mpd, pns);
    }

    public MPData toMPData(String js){
        MPData mpd = new MPData();
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode node = mapper.readTree(js);
            mpd.idclass = node.get(0).get("idclass").asText();
            int nid = node.get(0).get("ids").size();
            mpd.ids = new HashSet<>();
            for (int i = 0; i < nid; i++ ){
                mpd.ids.add(node.get(0).get("ids").get(i).asText());
            }
        }catch( IOException e ){
            e.printStackTrace();
        }
        return mpd;
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
