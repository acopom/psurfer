/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.dbcls.lodsurfer.psurfer;

import org.apache.jena.rdf.model.*;
import org.apache.jena.query.*;
import java.util.*;

/**
 *
 * @author atsuko
 */
public class MPQuery {
    static public MPOutput getResult(MPData mpd, ClassPath cp, Set<String> epc,
            Map<String, CInfo> cinfo){
        List<MPResult> res = getResult(mpd, cp, epc);
        return toMPOutput(res, cinfo);
    }
    
    static public void addResult(MPData mpd, ClassPath cp, Set<String> epc,
            Map<String, CInfo> cinfo, MPOutput mpo){
        List<MPResult> res = getResult(mpd, cp, epc);
        addMPOutput(res, cinfo, mpo);
    }
    
    static public List<MPResult> getResult(MPData mpd, ClassPath cp, Set<String> epc){
        List<MPResult> res = new LinkedList<>();
        Iterator<String> iit = mpd.ids.iterator();
        while ( iit.hasNext() ){
            String id = iit.next();
            MPResult mpr = doSPARQL(id, cp, epc);
            if (mpr != null){
                res.add(mpr);
            }
        }
        return res;
    }
    
    static public MPResult doSPARQL(String id, ClassPath cp, Set<String> epc){
        String prefix = "SELECT DISTINCT ?next WHERE{ ";
        
        Set<String> mid = new HashSet<>();
        mid.add(id);
        Set<String> next = new HashSet<>();
                
        for (int i = 0; i < cp.properties.size(); i++) {
            StringBuilder sparqlbuf = new StringBuilder(prefix);
            sparqlbuf.append("\n VALUES ?mid {");
            // mid val
            if (mid.isEmpty()){return null;} // no result}
            Iterator<String> mit = mid.iterator();
            while( mit.hasNext()){
                sparqlbuf.append("<").append(mit.next()).append("> ");
            }
            sparqlbuf.append("}\n");
            DiEdge de = cp.properties.get(i);
            if ( de.direction ){
                sparqlbuf.append("?mid <").append(de.property).append("> ?next .\n");
            }else{
                sparqlbuf.append("?next <").append(de.property).append("> ?mid .\n");                
            }
            if ( epc.contains(de.ep) ){ //use classes
                sparqlbuf.append("?mid a <").append(cp.classes.get(i));
                sparqlbuf.append(">\n ?next a <").append(cp.classes.get(i)).append(">\n");
            }
            sparqlbuf.append("}");
            String sparql = sparqlbuf.toString();
            //System.out.println(sparql);
            QueryExecution qe = QueryExecutionFactory.sparqlService(de.ep, sparql);
            ResultSet rs = qe.execSelect();
            while(rs.hasNext()){
                QuerySolution qs = rs.next();
                Resource re = qs.getResource("next");
                if ( re.isURIResource() ){
                    next.add(re.getURI());
                }
            }
            mid = next;
            next = new HashSet<>();
        }
        // mid is a set of results
        MPResult mpr = new MPResult();
        mpr.cl1 = cp.classes.get(0);
        mpr.cl2 = cp.classes.get(cp.classes.size()-1);
        mpr.id1 = id;
        mpr.id2 = mid;
        return mpr;
    }
    
    static public void addMPOutput(List<MPResult> res, Map<String, CInfo> cinfo, MPOutput mpo){        
        Map<String, Set<MPResult>> id2res = new HashMap<>();
        ListIterator<MPResult> rit = res.listIterator();
        while ( rit.hasNext()){
            MPResult mpr = rit.next();
            Set<MPResult> rs = id2res.get(mpr.id1);
            if ( rs == null ){
                rs = new HashSet<>();
            }
            rs.add(mpr);
            id2res.put(mpr.id1, rs);
        }
        Map<String, MPOutput.All> id2a = new HashMap<>();
        ListIterator<MPOutput.All> ait = mpo.all.listIterator();
        while (ait.hasNext()){
            MPOutput.All a = ait.next();
            MPOutput.All as = id2a.get(a.input.id);
            if ( as != null ){
                a = mergeAll(as, a);
            }
            //as.add(a);
            id2a.put(a.input.id, a);
        }
        
        Set<String> ids = id2res.keySet();
        Iterator<String> iit = ids.iterator();
        while ( iit.hasNext() ){
            String id = iit.next();
            Set<MPResult> rs = id2res.get(id); // all result for same id 
            MPOutput.All a = id2a.get(id); // mpo
            if ( a == null ){
                a = mpo.createOne(rs.iterator().next().cl1, id); // All
            }
            Iterator<MPResult> crit = rs.iterator();
            while ( crit.hasNext() ){
                MPResult r = crit.next();
                MPOutput.OutputForClass oc = mpo.createOC(a, r.cl2); // OutputForClass
                CInfo ci = cinfo.get(r.cl2);
                Iterator<String> iit2 = r.id2.iterator();
                while( iit2.hasNext() ){
                    String id2 = iit2.next();
                    MPOutput.Result or = mpo.createResult(oc, id2);
                    // get labels
                    or.label = getLabel(ci, id2);
                    or.info = getInfo(ci, id2);
                }
            }
            mpo.all.add(a);
        }
    }
    
    static public MPOutput toMPOutput(List<MPResult> res, Map<String, CInfo> cinfo){
        MPOutput mpo = new MPOutput();
        addMPOutput(res, cinfo, mpo);
        return mpo;
    }
    
    static public String getLabel(CInfo ci, String id){
        return getLiteral(ci.ep, id, ci.labelp);
    }
    
    static public String getInfo(CInfo ci, String id){
        return getLiteral(ci.ep, id, ci.infop);
    }
    
    static public String getLiteral(String ep, String id, String prop){
        String prefix = "SELECT ?literal WHERE { \n <";
        StringBuilder sparqlbuf = new StringBuilder(prefix);
        sparqlbuf.append(id).append("> <").append(prop).append("> ?literal . }\n");
        
        String sparql = sparqlbuf.toString();
        //System.out.println(sparql);
        QueryExecution qe = QueryExecutionFactory.sparqlService(ep, sparql);
        ResultSet rs = qe.execSelect();
        while(rs.hasNext()){
            QuerySolution qs = rs.next();
            RDFNode re = qs.get("literal"); //.getResource("literal");            
            if ( re.isLiteral() ){
                return re.asLiteral().getString();
            }
        }
        return null;
    }
    
    static public MPOutput.All mergeAll(MPOutput.All a1, MPOutput.All a2){
        if ( !a1.input.idclass.equals(a2.input.idclass )
                || !a1.input.id.equals(a2.input.id)){
            System.err.println("id or class unmatched!");
            return a1;
        }
        MPOutput.All a = new MPOutput().createOne(a1.input.idclass, a1.input.id);
        a.output.addAll(a1.output);
        a.output.addAll(a2.output);
        return a;
    }
    
}