package controller;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import sessao.UsuarioSessao;

@Controller
public class IndexController {

	private final Result result;
        
        @Inject
        private UsuarioSessao usuarioSessao;
        

	/**
	 * @deprecated CDI eyes only
	 */
	protected IndexController() {
		this(null);
	}
	
	@Inject
	public IndexController(Result result) {
		this.result = result;
	}

        
	@Path("/")
	public void index() {
//		result.include("variable", "Download e Upload");
	}
        
        
        //        
         
        @Get
        @Path("/index/destruir")
        public void destruir(){ 
            this.usuarioSessao.setLogin("");
            this.usuarioSessao.setSenha("");
            this.result.redirectTo(this).index();
        }
        
        @Post
        @Path("/index/login")
        public void login(String login, String senha){
               if (login.equals("login") && senha.equals("senha")){
                   this.usuarioSessao.setLogin(login);
                   this.usuarioSessao.setSenha(senha);
                   this.result.redirectTo(ArquivoController.class).listar();                   
               } else {
                   this.result.include("variable", "erro no login");
                   this.result.redirectTo(this).index();
               }
        }
}