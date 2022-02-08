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
        List<String> pns = new LinkedList<>();
        pns.add("Taxon");
        pns.add("GO");
        pns.add("ActiveSiteUniProtBegin");
        pns.add("VariantUniProtRegion");
        
        //String js = mpc.getResultFromClasses("search.json", cls);
        String js = mpc.getResultFromPNames(args[0], pns);
        //String js = mpc.getResultFromPNames("search.json", pns);     
        //MPIO.writeJson2File(js, "out6.json");
        MPIO.writeJson2File(js, args[1]);
    }
}
