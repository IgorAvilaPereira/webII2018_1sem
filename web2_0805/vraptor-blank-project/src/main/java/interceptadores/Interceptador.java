/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptadores;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import controller.IndexController;
import javax.inject.Inject;
import sessao.UsuarioSessao;

/**
 *
 * @author iapereira
 */
@Intercepts
public class Interceptador {
    
    @Inject 
    private UsuarioSessao usuarioSessao;
    
    @Inject
    private Result result;
    
    
    @Accepts
    public boolean accepts(ControllerMethod m){
        return ! m.getController().getType().equals(IndexController.class);
        
    }
    
    @BeforeCall
    public void before(){
        if (!(this.usuarioSessao.getLogin().equals("login") && this.usuarioSessao.getSenha().equals("senha"))){
            this.result.redirectTo(IndexController.class).index();
        }
    }
    
}
