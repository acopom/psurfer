/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.dbcls.lodsurfer.psurfer;

import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author atsuko
 */
public class MPResult {
    String cl1;
    String id1;
    String cl2;
    Set<String> id2;
    
    public MPResult(){
        id2 = new HashSet<>();
    }
}
