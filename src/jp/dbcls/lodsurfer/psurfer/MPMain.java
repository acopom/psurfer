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
        String ijs = MPIO.read(args[0]);
        String ojs = getResult(ijs);
        //MPIO.writeJson2File(js, "out2.json");
        MPIO.writeJson2File(ojs, args[1]);    
    }
    
    public static String getResult(String ijs){
        MPCtrl mpc = new MPCtrl();
        mpc.init();
        
        List<String> pns = new LinkedList<>();
        pns.add("Taxon");
        pns.add("GO");
        //pns.add("ActiveSiteUniProtBegin");
        //pns.add("VariantUniProtRegion");
        
        MPData mpd = mpc.toMPData(ijs);
        String ojs = mpc.getResultFromPNames(mpd, pns);
        return ojs;
    }
}
