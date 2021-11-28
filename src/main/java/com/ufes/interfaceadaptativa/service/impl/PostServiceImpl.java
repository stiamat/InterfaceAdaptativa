package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Optional;
import java.util.Set;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    public PostDTO save(PostDTO postDTO) {
        log.debug("Request to save Post : {}", postDTO);
        Post post = postMapper.toEntity(postDTO);
        post = postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable)
            .map(postMapper::toDto);
    }


    public Page<PostDTO> findAllWithEagerRelationships(Pageable pageable) {
        return postRepository.findAllWithEagerRelationships(pageable).map(postMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> findOne(Long id) {
        this.owl();
        log.debug("Request to get Post : {}", id);
        return postRepository.findOneWithEagerRelationships(id)
            .map(postMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }

    public void owl() {
        try {
            // Gerenciador da ontologia - carrega funções e propriedade para podermos trabalhar com as ontologias
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            // Input do arquivo owl e criando OWLOntology
            File ontologyFile = new File("D:\\HD-Downloads\\Ontoligia_Alexandre.owl");
            OWLOntology ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);

            // Tentando adicionar um individual
            IRI ontologyIRI = IRI.create("http://www.semanticweb.org/interface-adaptativa");
            OWLDataFactory factory = manager.getOWLDataFactory();

            // Carregando classes de interesse
            OWLClass font_Increase = manager.getOWLDataFactory().getOWLClass(IRI.create(ontologyIRI + "#Font_Increase"));
            OWLClass font_Decrease = manager.getOWLDataFactory().getOWLClass(IRI.create(ontologyIRI + "#Font_Decrease"));

            OWLIndividual i02 = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#02"));
            OWLDataPropertyExpression has_Age= factory.getOWLDataProperty(IRI.create(ontologyIRI + "#has_Age"));
            OWLDataPropertyAssertionAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(has_Age, i02, 55);
            AddAxiom addAxiom = new AddAxiom(ontology, axiom);

            manager.applyChange(addAxiom);
            // salvar individuo na ontologia (not necessario)
            manager.saveOntology(ontology);

            //

            // Reasoner
            ReasonerFactory reasonerFactory = new ReasonerFactory();
            Configuration configuration=new Configuration();
            configuration.throwInconsistentOntologyException=false;
            OWLReasoner reasoner= reasonerFactory.createReasoner(ontology, configuration);

            for (OWLClass c : ontology.getClassesInSignature()) {
                if (c.getIRI().getFragment().equals("Basic_Experience_Mode")){
                    NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
                    System.out.println(c.getIRI().getFragment());
                    for (OWLNamedIndividual i : instances.getFlattened()) {
                        System.out.println(i.getIRI().getFragment());
                    }
                }
            }

            // Teste de consistencia da ontologia
            boolean consistent = reasoner.isConsistent();
            System.out.println(consistent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
