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
        String js = getResult(args[0]);
        //MPIO.writeJson2File(js, "out2.json");
        MPIO.writeJson2File(js, args[1]);    
    }
    
    public static String getResult(String in){
        MPCtrl mpc = new MPCtrl();
        mpc.init();
        
        List<String> pns = new LinkedList<>();
        pns.add("Taxon");
        pns.add("GO");
        //pns.add("ActiveSiteUniProtBegin");
        //pns.add("VariantUniProtRegion");

        String js = mpc.getResultFromPNames(in, pns);
        //String js = mpc.getResultFromPNames("search.json", pns);
        return js;
    }
}
