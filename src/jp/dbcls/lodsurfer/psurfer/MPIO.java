/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.dbcls.lodsurfer.psurfer;

import java.io.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;

/**
 *
 * @author atsuko
 */
public class MPIO {
    
    static public MPData toMPData(String filename){
        File ifile = new File(filename);
        MPData mpd = new MPData();
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode node = mapper.readTree(ifile);
            //JsonNode n = node.get("idclass");
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

    static public List<ClassPath> readPaths(String filename){
        List<ClassPath> cps = new LinkedList<>();
        File ifile = new File(filename);
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode node = mapper.readTree(ifile);
            int npath = node.size();
            for ( int i = 0; i < npath ; i++ ){
                ClassPath cp = new ClassPath();
                cp.pname = node.get(i).get("pname").asText();
                int nc = node.get(i).get("classes").size();
                for ( int j = 0; j < nc ; j++ ){
                    cp.classes.add(node.get(i).get("classes").get(j).asText());
                }
                int np = node.get(i).get("properties").size();
                for ( int j = 0; j < np ; j++ ){
                    DiEdge de = new DiEdge(
                            node.get(i).get("properties").get(j).get("property").asText(),
                            node.get(i).get("properties").get(j).get("direction").asBoolean(),
                            node.get(i).get("properties").get(j).get("ep").asText()
                            );
                    cp.properties.add(de);
                }
                cps.add(cp);
            }
        }catch( IOException e ){
            e.printStackTrace();
        }
        return cps;
    }
    
    static public Map<String, ClassPath> readNPaths(String filename){
        Map<String, ClassPath> cps = new HashMap<>();
        File ifile = new File(filename);
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode node = mapper.readTree(ifile);
            int npath = node.size();
            for ( int i = 0; i < npath ; i++ ){
                ClassPath cp = new ClassPath();
                cp.pname = node.get(i).get("pname").asText();
                int nc = node.get(i).get("classes").size();
                for ( int j = 0; j < nc ; j++ ){
                    cp.classes.add(node.get(i).get("classes").get(j).asText());
                }
                int np = node.get(i).get("properties").size();
                for ( int j = 0; j < np ; j++ ){
                    DiEdge de = new DiEdge(
                            node.get(i).get("properties").get(j).get("property").asText(),
                            node.get(i).get("properties").get(j).get("direction").asBoolean(),
                            node.get(i).get("properties").get(j).get("ep").asText()
                            );
                    cp.properties.add(de);
                }
                cps.put(cp.pname,cp);
            }
        }catch( IOException e ){
            e.printStackTrace();
        }
        return cps;
    }

    static public Set<String> readEPs(String filename){
        Set<String> eps = new HashSet<>();
        File ifile = new File(filename);
        try{
            BufferedReader br = new BufferedReader(new FileReader(ifile));
            String buf = br.readLine();
            while(buf != null ){
                eps.add(buf);
                buf = br.readLine();
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return eps;
    }
    
    static public Map<String, CInfo> readCInfo(String filename){
        Map<String, CInfo> ci = new HashMap<>();
        File ifile = new File(filename);
        try{
            BufferedReader br = new BufferedReader(new FileReader(ifile));
            String buf = br.readLine();
            while(buf != null ){
                //String data[] = buf.split("\t"); // curl, label, ep, labelp, infop
                String data[] = buf.split(","); // curl, label, ep, labelp, infop, supclass
                //System.out.println(buf);
                CInfo i = new CInfo(data[0],data[1],data[2],data[3],data[4],data[5]);
                ci.put(data[0],i);
                buf = br.readLine();
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return ci;
    }
    
    static public void writeJson2File(String json, String filename){
        File file = new File(filename);
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(json);
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
}
