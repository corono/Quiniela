/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

/**
 *
 * @author francisco
 */
public class ClasRow {
    
    private Integer equipo;
    private String temporada;
    private Integer jornada;
    private String division;
    private Integer pj;
    private Integer pg;
    private Integer pp;
    private Integer pe;
    private Integer pgl;
    private Integer ppl;
    private Integer pel;
    private Integer pgv;
    private Integer ppv;
    private Integer pev;
    private Integer gf;
    private Integer gc;
    private Integer gfl;
    private Integer gfv;
    private Integer gcl;
    private Integer gcv;
    
    
    public ClasRow(Integer id, String season, Integer jor, String div){
        equipo = id;
        temporada = season;
        jornada = jor;
        division = div;
        pj = 0;
        pg = 0;
        pe = 0;
        pp = 0;
        pgl = 0;
        ppl = 0;
        pel = 0;
        pgv = 0;
        ppv = 0;
        pev = 0;
        gf = 0;
        gc = 0;
        gfl = 0;
        gfv = 0;
        gcl = 0;
        gcv = 0;
        
    }  
    
    public void setLocalMatch (int[] resultado){
        
        pj++;
        
        switch(resultado[0]){
            case 0://derrota local
                ppl++;
                pp++;
                gf += resultado[1];
                gc += resultado[2];
                gfl += resultado[1];
                gcl += resultado[2];
                break;
            case 1://empate
                pel++;
                pe++;
                gf += resultado[1];
                gc += resultado[2];
                gfl += resultado[1];
                gcl += resultado[2];
                break;
            case 2://victoria local                
                pgl++;
                pg++;
                gf += resultado[1];
                gc += resultado[2];
                gfl += resultado[1];
                gcl += resultado[2];
                break;
            default:
                System.out.println("Error saving info local match for clasrow");
                break;
            
        }
    }
    
    public void setVisitorMatch(int[] resultado){

         pj++;
        
        switch(resultado[0]){
            case 0://victoria visitante
                pgv++;
                pg++;
                gf += resultado[1];
                gc += resultado[2];
                gfv += resultado[1];
                gcv += resultado[2];
                break;
            case 1://empate
                pev++;
                pe++;
                gf += resultado[1];
                gc += resultado[2];
                gfv += resultado[1];
                gcv += resultado[2];
                break;
            case 2://derrota visitante                
                ppv++;
                pp++;
                gf += resultado[1];
                gc += resultado[2];
                gfv += resultado[1];
                gcv += resultado[2];
                break;
            default:
                System.out.println("Error saving info visitor match for clasrow");
                break;
            
        }
        
    }
    
    public void acumulado(ClasRow anterior){
        
        this.pj += anterior.getPJ();
        this.pg += anterior.getPG();
        this.pp += anterior.getPP();
        this.pe += anterior.getPE();
        this.pgl += anterior.getPGL();
        this.ppl += anterior.getPPL();
        this.pel += anterior.getPEL();
        this.pgv += anterior.getPGV();
        this.ppv += anterior.getPPV();
        this.pev += anterior.getPEV();
        this.gf += anterior.getGF();
        this.gc += anterior.getGC();
        this.gfl += anterior.getGFL();
        this.gcl += anterior. getGCL();
        this.gfv += anterior.getGFV();
        this.gcv += anterior.getGCV();

    }
    public int getPJ(){
        return this.pj;
    }
    public int getPG(){
        return this.pg;
    }
    public int getPP(){
        return this.pp;
    }
    public int getPE(){
        return this.pe;
    }
    public int getPGL(){
        return this.pgl;
    }
    public int getPPL(){
        return this.ppl;
    }
    public int getPEL(){
        return this.pel;
    }
    public int getPGV(){
        return this.pgv;
    }
    public int getPPV(){
        return this.ppv;        
    }
    public int getPEV(){
        return this.pev;
    }
    public int getGF(){
        return this.gf;
    }
    public int getGC(){
        return this.gc;
    }
    public int getGFL(){
        return this.gfl;
    }
    public int getGCL(){
        return this.gcl;
    }
    public int getGFV(){
        return this.gfv;
    }
    public int getGCV(){
        return this.gcv;
    }
    public String getTemporada(){
        return this.temporada;
    }
    public Integer getJornada(){
        return this.jornada;
    }
    public String getDivision(){
        return this.division;
    }
    public Integer getEquipo(){
        return this.equipo;
    }
}
