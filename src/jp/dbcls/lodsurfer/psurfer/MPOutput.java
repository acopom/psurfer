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
public class MPOutput {
    public List<All> all;
    public class All{ // all result for one id 
        public Input input;
        public List<OutputForClass> output;
    }
    
    public class Input{ // one id
        public String idclass;
        public String id;
    }
    
    public class OutputForClass{ 
        public String idclass;
        public List<Result> result;
    }
    
    public class Result{
        public String id;
        public String label;
        public String info;
    }
    
    public MPOutput(){
        all = new LinkedList<>();
    }
    
    public All createOne(String idclass, String id ){
        All a = new All();
        a.input = new Input();
        a.input.idclass = idclass;
        a.input.id = id;
        a.output = new LinkedList<>(); 
        return a;
    }
    
    public OutputForClass createOC(All a, String idclass){
        OutputForClass oc = new OutputForClass();
        oc.idclass = idclass;
        oc.result = new LinkedList<>();
        a.output.add(oc);
        return oc;
    }
    
    public Result createResult(OutputForClass oc, String id){
        Result res = new Result();
        res.id = id;
        oc.result.add(res);
        
        return res;
    }
    
}
