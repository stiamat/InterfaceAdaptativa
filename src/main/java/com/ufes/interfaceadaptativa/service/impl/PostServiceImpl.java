package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.domain.Profile;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.repository.ProfileRepository;
import com.ufes.interfaceadaptativa.repository.UserRepository;
import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.dto.ProfileDTO;
import com.ufes.interfaceadaptativa.service.dto.UserDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;
import com.ufes.interfaceadaptativa.service.mapper.UserMapper;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Post}.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PostMapper postMapper;

    private final ProfileRepository profileRepository;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository, UserMapper userMapper, ProfileRepository profileRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.profileRepository = profileRepository;
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
        this.owl(3);
        log.debug("Request to get Post : {}", id);
        return postRepository.findOneWithEagerRelationships(id)
            .map(postMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> curti(long postId, long userId) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();
        Set<User> likes = post.getLikeDes();

        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }

        post.setLikeDes(likes);
        return userMapper.usersToUserDTOs(likes.stream().collect(Collectors.toList()));
    }



    public void owl(long idUser) {
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
            Map<String, OWLClass> ClassesOWL = this.loadingClassOWL(manager, ontologyIRI);

            //Carregando Individuo para inferencia
            this.loadingIndividual(factory, ontologyIRI, ontology, manager, idUser);


            // Reasoner
            ReasonerFactory reasonerFactory = new ReasonerFactory();
            Configuration configuration = new Configuration();
            configuration.throwInconsistentOntologyException = false;
            OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, configuration);

            // Teste de consistencia da ontologia
            boolean consistent = reasoner.isConsistent();

            this.inferindoSobreIndividuo(ontology, reasoner);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, OWLClass> loadingClassOWL(OWLOntologyManager manager, IRI ontologyIRI) {
        Map<String, OWLClass> classesOWL = new HashMap<String, OWLClass>();

        OWLClass font_Increase = manager.getOWLDataFactory().getOWLClass(IRI.create(ontologyIRI + "#Font_Increase"));
        classesOWL.put("Font_Increase", font_Increase);

        OWLClass font_Decrease = manager.getOWLDataFactory().getOWLClass(IRI.create(ontologyIRI + "#Font_Decrease"));
        classesOWL.put("Font_Increase", font_Increase);

        return classesOWL;
    }

    public void loadingIndividual(OWLDataFactory factory, IRI ontologyIRI, OWLOntology ontology, OWLOntologyManager manager, long idUser) {
        OWLIndividual individuo = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#" + idUser));

        // idade
        OWLDataPropertyExpression has_Age = factory.getOWLDataProperty(IRI.create(ontologyIRI + "#has_Age"));
        OWLDataPropertyAssertionAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(has_Age, individuo, 55);
        AddAxiom addAxiom = new AddAxiom(ontology, axiom);
        manager.applyChange(addAxiom);

        // Basic Experience
//        OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#has_Disabilities"));
//        OWLIndividual lightVision = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Light_Sensitivy"));
//        OWLObjectPropertyAssertionAxiom newaxiom = factory.getOWLObjectPropertyAssertionAxiom(has_Disabilities, individuo, lightVision);
//        addAxiom = new AddAxiom(ontology, newaxiom);
//        manager.applyChange(addAxiom);

        // Light Vision
        OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#has_Disabilities"));
        OWLIndividual lightVision = factory.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Light_Sensitivy"));
        OWLObjectPropertyAssertionAxiom newaxiom = factory.getOWLObjectPropertyAssertionAxiom(has_Disabilities, individuo, lightVision);
        addAxiom = new AddAxiom(ontology, newaxiom);
        manager.applyChange(addAxiom);

        // necessidades especiais

        // salvar individuo na ontologia (não é necessario necessario)
        // manager.saveOntology(ontology);
    }

    public void inferindoSobreIndividuo(OWLOntology ontology, OWLReasoner reasoner) {

        for (OWLClass c : ontology.getClassesInSignature()) {
            // find Basic_Experience_Mode
            if (c.getIRI().getShortForm().equals("Basic_Experience_Mode")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }

            // find Average_Experience_Mode
            if (c.getIRI().getShortForm().equals("Average_Experience_Mode")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }

            // find High_Experience_Mode
            if (c.getIRI().getShortForm().equals("High_Experience_Mode")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }

            // find Font_Decrease
            if (c.getIRI().getShortForm().equals("Font_Decrease")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }

            // find Font_Increase
            if (c.getIRI().getShortForm().equals("Font_Increase")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }

            // find Dark_Mode
            if (c.getIRI().getShortForm().equals("Dark_Mode")) {
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    System.out.println(i.getIRI().getShortForm().split("#")[1] + " - " + c.getIRI().getShortForm());
                }
            }
        }

    }
}
