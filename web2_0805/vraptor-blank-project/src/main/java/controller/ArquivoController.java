/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.inject.Inject;
import sessao.UsuarioSessao;

/**
 *
 * @author iapereira
 */
@Controller
public class ArquivoController {
    
    @Inject
    private Result result;
    @Inject
    private UsuarioSessao usuarioSessao;
    
    @Get
    @Path("/arquivo/tela_adicionar")
    public void tela_adicionar(){
//        this.result.include("mensagem", "");
    }
    
    @Post
    @Path("/arquivo/adicionar")
    public void adicionar(UploadedFile arquivo) throws IOException{
        String pathName = "/home/iapereira/web2_0805/vraptor-blank-project/src/main/webapp/WEB-INF/arquivos/";
        if (null != arquivo){            
            if (arquivo.getContentType().trim().equals("image/png") || arquivo.getContentType().trim().equals("image/jpeg")){
                File f = new File(pathName + Calendar.getInstance().getTimeInMillis() + (arquivo.getContentType().trim().equals("image/png") ? ".png" : ".jpg"));
                arquivo.writeTo(f);   
                this.result.include("mensagem", "Sucesso....");
                this.result.redirectTo(ArquivoController.class).tela_adicionar();
            } else {
                 this.result.include("mensagem", "Tipo de dado invalido...");
                this.result.redirectTo(ArquivoController.class).tela_adicionar();
            }            
        } else {
            this.result.include("mensagem", "Nenhum arquivo");
            this.result.redirectTo(ArquivoController.class).tela_adicionar();
        }
        
    }
    
    @Get
    @Path("/arquivo/visualizar/{nome}")
    public File visualizar(String nome){
        String pathName = "/home/iapereira/web2_0805/vraptor-blank-project/src/main/webapp/WEB-INF/arquivos/";
        return new File(pathName + nome);  
    }
   
    @Get
    @Path("/arquivo/excluir/{nome}")
    public void excluir(String nome){
        String pathName = "/home/iapereira/web2_0805/vraptor-blank-project/src/main/webapp/WEB-INF/arquivos/";
        File f = new File(pathName + nome);  
        f.delete();
        this.result.redirectTo(this).listar();        

    }
    
    @Get
    @Path("/arquivo/listar")
    public void listar(){
        this.result.include("usuarioSessao", usuarioSessao);
         String pathName = "/home/iapereira/web2_0805/vraptor-blank-project/src/main/webapp/WEB-INF/arquivos/";
        File diretorio = new File(pathName);
        this.result.include("vetArquivo", diretorio.listFiles());
        
        
    }
    
}
