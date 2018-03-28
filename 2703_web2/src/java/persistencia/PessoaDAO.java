/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Endereco;
import modelo.Dependente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Pessoa;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author iapereira
 */
public class PessoaDAO implements Serializable {

    public PessoaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) {
        if (pessoa.getDependenteList() == null) {
            pessoa.setDependenteList(new ArrayList<Dependente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Endereco enderecoId = pessoa.getEnderecoId();
            if (enderecoId != null) {
                enderecoId = em.getReference(enderecoId.getClass(), enderecoId.getId());
                pessoa.setEnderecoId(enderecoId);
            }
            List<Dependente> attachedDependenteList = new ArrayList<Dependente>();
            for (Dependente dependenteListDependenteToAttach : pessoa.getDependenteList()) {
                dependenteListDependenteToAttach = em.getReference(dependenteListDependenteToAttach.getClass(), dependenteListDependenteToAttach.getId());
                attachedDependenteList.add(dependenteListDependenteToAttach);
            }
            pessoa.setDependenteList(attachedDependenteList);
            em.persist(pessoa);
            if (enderecoId != null) {
                enderecoId.getPessoaList().add(pessoa);
                enderecoId = em.merge(enderecoId);
            }
            for (Dependente dependenteListDependente : pessoa.getDependenteList()) {
                Pessoa oldPessoaIdOfDependenteListDependente = dependenteListDependente.getPessoaId();
                dependenteListDependente.setPessoaId(pessoa);
                dependenteListDependente = em.merge(dependenteListDependente);
                if (oldPessoaIdOfDependenteListDependente != null) {
                    oldPessoaIdOfDependenteListDependente.getDependenteList().remove(dependenteListDependente);
                    oldPessoaIdOfDependenteListDependente = em.merge(oldPessoaIdOfDependenteListDependente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getId());
            Endereco enderecoIdOld = persistentPessoa.getEnderecoId();
            Endereco enderecoIdNew = pessoa.getEnderecoId();
            List<Dependente> dependenteListOld = persistentPessoa.getDependenteList();
            List<Dependente> dependenteListNew = pessoa.getDependenteList();
            if (enderecoIdNew != null) {
                enderecoIdNew = em.getReference(enderecoIdNew.getClass(), enderecoIdNew.getId());
                pessoa.setEnderecoId(enderecoIdNew);
            }
            List<Dependente> attachedDependenteListNew = new ArrayList<Dependente>();
            for (Dependente dependenteListNewDependenteToAttach : dependenteListNew) {
                dependenteListNewDependenteToAttach = em.getReference(dependenteListNewDependenteToAttach.getClass(), dependenteListNewDependenteToAttach.getId());
                attachedDependenteListNew.add(dependenteListNewDependenteToAttach);
            }
            dependenteListNew = attachedDependenteListNew;
            pessoa.setDependenteList(dependenteListNew);
            pessoa = em.merge(pessoa);
            if (enderecoIdOld != null && !enderecoIdOld.equals(enderecoIdNew)) {
                enderecoIdOld.getPessoaList().remove(pessoa);
                enderecoIdOld = em.merge(enderecoIdOld);
            }
            if (enderecoIdNew != null && !enderecoIdNew.equals(enderecoIdOld)) {
                enderecoIdNew.getPessoaList().add(pessoa);
                enderecoIdNew = em.merge(enderecoIdNew);
            }
            for (Dependente dependenteListOldDependente : dependenteListOld) {
                if (!dependenteListNew.contains(dependenteListOldDependente)) {
                    dependenteListOldDependente.setPessoaId(null);
                    dependenteListOldDependente = em.merge(dependenteListOldDependente);
                }
            }
            for (Dependente dependenteListNewDependente : dependenteListNew) {
                if (!dependenteListOld.contains(dependenteListNewDependente)) {
                    Pessoa oldPessoaIdOfDependenteListNewDependente = dependenteListNewDependente.getPessoaId();
                    dependenteListNewDependente.setPessoaId(pessoa);
                    dependenteListNewDependente = em.merge(dependenteListNewDependente);
                    if (oldPessoaIdOfDependenteListNewDependente != null && !oldPessoaIdOfDependenteListNewDependente.equals(pessoa)) {
                        oldPessoaIdOfDependenteListNewDependente.getDependenteList().remove(dependenteListNewDependente);
                        oldPessoaIdOfDependenteListNewDependente = em.merge(oldPessoaIdOfDependenteListNewDependente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getId();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            Endereco enderecoId = pessoa.getEnderecoId();
            if (enderecoId != null) {
                enderecoId.getPessoaList().remove(pessoa);
                enderecoId = em.merge(enderecoId);
            }
            List<Dependente> dependenteList = pessoa.getDependenteList();
            for (Dependente dependenteListDependente : dependenteList) {
                dependenteListDependente.setPessoaId(null);
                dependenteListDependente = em.merge(dependenteListDependente);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
