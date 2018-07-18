/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.dbcls.lodsurfer.psurfer;

/**
 *
 * @author atsuko
 */
//import com.fasterxml.jackson.databind.*;
import java.util.*;

public class MPMain {

    //String infile = "./inf.json";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MPCtrl mpc = new MPCtrl();
        mpc.init();
        
        //System.out.println(mpc.getResult("search.json", "http://purl.uniprot.org/core/Taxon"));
        List<String> cls = new LinkedList<>();
        cls.add("http://purl.uniprot.org/core/Taxon");
        //cls.add("http://purl.uniprot.org/core/Concept");
        cls.add("http://www.w3.org/2002/07/owl#Class");
        cls.add("http://biohackathon.org/resource/faldo#ExactPosition");
        
        //String js = mpc.getResultFromClasses("search.json", cls);
        String js = mpc.getResultFromClasses(args[0], cls);
        //MPIO.writeJson2File(js, "out5.json");
        MPIO.writeJson2File(js, args[1]);
    }
}
