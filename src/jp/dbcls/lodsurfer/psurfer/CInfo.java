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
public class CInfo {
   public String classURI;
   public String label;
   public String ep;
   public String labelp; //property for label
   public String infop; //property for info
   
   public CInfo(String curl, String label, String ep, String labelp, String infop){
       classURI = curl;
       this.label = label;
       this.ep = ep;
       this.labelp = labelp;
       this.infop = infop;
   }
}
