/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.dbcls.lodsurfer.psurfer;

import java.util.*;

/**
 *
 * @author atsuko
 */

public class ClassPath {
    String pname;
    List<String> classes;
    List<DiEdge> properties;
    
    public ClassPath(){
        classes = new LinkedList<>();
        properties = new LinkedList<>(); 
    } 
}
