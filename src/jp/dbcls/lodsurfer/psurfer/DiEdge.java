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
public class DiEdge {    
    boolean direction; // true: forward, false: backward
    //ClassRelation cr;    
    String property; // url
    String ep; // ep URL
    
    public DiEdge(String prop, boolean direction, String ep){
        //this.node = node;
        this.property = prop;
        this.direction = direction;
        this.ep = ep;
    }
    
    /*
    @Override
    public boolean equals(Object ob){
        if ( this == ob ){ return true; }
        if ( ob instanceof DiEdge){
            DiEdge di = (DiEdge) ob;
            if ( cr.equals(di.cr) && direction == di.direction ){
                return true;
            }
        }
        return false;
    }*/
}
