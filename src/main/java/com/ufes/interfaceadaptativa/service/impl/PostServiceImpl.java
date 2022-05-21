package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.domain.Profile;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.repository.ProfileRepository;
import com.ufes.interfaceadaptativa.repository.UserRepository;
import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.dto.UserDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;
import com.ufes.interfaceadaptativa.service.mapper.UserMapper;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  private final PreferencesServiceImpl preferencesServiceImpl;

  public PostServiceImpl(
    PostRepository postRepository,
    PostMapper postMapper,
    UserRepository userRepository,
    UserMapper userMapper,
    ProfileRepository profileRepository,
    PreferencesServiceImpl preferencesServiceImpl
  ) {
    this.postRepository = postRepository;
    this.postMapper = postMapper;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.profileRepository = profileRepository;
    this.preferencesServiceImpl = preferencesServiceImpl;
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
    return postRepository.findAll(pageable).map(postMapper::toDto);
  }

  public Page<PostDTO> findAllWithEagerRelationships(Pageable pageable) {
    return postRepository
      .findAllWithEagerRelationships(pageable)
      .map(postMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PostDTO> findOne(Long id) {
    this.owl(3);
    log.debug("Request to get Post : {}", id);
    return postRepository
      .findOneWithEagerRelationships(id)
      .map(postMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Post : {}", id);
    this.postRepository.findAnswersPost(id)
      .ifPresent(
        l -> {
          l.forEach(
            p -> {
              postRepository.deleteById(p.getId());
            }
          );
        }
      );
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
    return userMapper.usersToUserDTOs(
      likes.stream().collect(Collectors.toList())
    );
  }

  @Override
  public void owl(long idUser) {
    try {
      // Gerenciador da ontologia - carrega funções e propriedade para podermos trabalhar com as ontologias
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      boolean server = false;
      // Input do arquivo owl e criando OWLOntology
      String path = "";
      if (server) {
        path =
          "./webapps/ROOT/WEB-INF/classes/static/content/ontoSNOPI/ontoSNOPI.owl";
      } else {
        path = "src/main/webapp/content/ontoSNOPI/ontoSNOPI.owl";
      }

      File ontologyFile = new File(Paths.get(path).toString());
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(
        ontologyFile
      );

      // Tentando adicionar um individual
      IRI ontologyIRI = IRI.create(
        "http://www.semanticweb.org/interface-adaptativa"
      );
      OWLDataFactory factory = manager.getOWLDataFactory();

      //Carregando Individuo para inferencia
      this.loadingIndividual(factory, ontologyIRI, ontology, manager, idUser);

      // Reasoner
      ReasonerFactory reasonerFactory = new ReasonerFactory();
      Configuration configuration = new Configuration();
      configuration.throwInconsistentOntologyException = false;
      OWLReasoner reasoner = reasonerFactory.createReasoner(
        ontology,
        configuration
      );

      // Teste de consistencia da ontologia
      boolean consistent = reasoner.isConsistent();

      this.inferindoSobreIndividuo(ontology, reasoner);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void loadingIndividual(
    OWLDataFactory factory,
    IRI ontologyIRI,
    OWLOntology ontology,
    OWLOntologyManager manager,
    long idUser
  ) {
    Profile profile = profileRepository.findById(idUser).get();
    preferencesServiceImpl.clearPreferences(idUser);
    OWLIndividual individuo = factory.getOWLNamedIndividual(
      IRI.create(ontologyIRI + "#" + idUser)
    );

    if (profile.getAge() != null) {
      // idade
      OWLDataPropertyExpression has_Age = factory.getOWLDataProperty(
        IRI.create(ontologyIRI + "#has_Age")
      );
      OWLDataPropertyAssertionAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(
        has_Age,
        individuo,
        profile.getAge()
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isAuditoryDisabilities()) {
      // problemas auditivos
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Auditory_Disabilities")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isBlindness()) {
      // cegueira
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Blindness")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isColorVision()) {
      // daltonismo
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Color_Vision")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isContrastSensitivity()) {
      // sensibilidade ao contraste
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Contrast_Sensitivity")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isFildOfVision()) {
      // sensibilidade ao contraste
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Fild_Of_Vision")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isLightSensitivity()) {
      // sensibilidade a luminosidade
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Light_Sensitivy")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.isVisualAcuity()) {
      // miopia
      OWLObjectPropertyExpression has_Disabilities = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Disabilities")
      );
      OWLIndividual disabilities = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Visual_Acuity")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Disabilities,
        individuo,
        disabilities
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }

    if (profile.getExperienceLevel().equals("BASICMODE")) {
      // experiencia Basica
      OWLObjectPropertyExpression has_Experience_Level = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Experience_Level")
      );
      OWLIndividual experience_Level = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Basic_Experience")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Experience_Level,
        individuo,
        experience_Level
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    } else if (profile.getExperienceLevel().equals("AVERAGEMODE")) {
      // experiencia Media
      OWLObjectPropertyExpression has_Experience_Level = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Experience_Level")
      );
      OWLIndividual experience_Level = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#Average_Experience")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Experience_Level,
        individuo,
        experience_Level
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    } else if (profile.getExperienceLevel().equals("HIGHMODE")) {
      // experiencia Alta
      OWLObjectPropertyExpression has_Experience_Level = factory.getOWLObjectProperty(
        IRI.create(ontologyIRI + "#has_Experience_Level")
      );
      OWLIndividual experience_Level = factory.getOWLNamedIndividual(
        IRI.create(ontologyIRI + "#High_Experience")
      );
      OWLObjectPropertyAssertionAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(
        has_Experience_Level,
        individuo,
        experience_Level
      );
      AddAxiom addAxiom = new AddAxiom(ontology, axiom);
      manager.applyChange(addAxiom);
    }
    // salvar individuo na ontologia (não é necessário neste neste estudo)
    // manager.saveOntology(ontology);
  }

  public void inferindoSobreIndividuo(
    OWLOntology ontology,
    OWLReasoner reasoner
  ) {
    for (OWLClass c : ontology.getClassesInSignature()) {
      // find Basic_Experience_Mode
      if (c.getIRI().getShortForm().equals("Basic_Experience_Mode")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner("Basic_Mode", id);
        }
      }

      // find Average_Experience_Mode
      if (c.getIRI().getShortForm().equals("Average_Experience_Mode")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner(
            "Average_Mode",
            id
          );
        }
      }

      // find High_Experience_Mode
      if (c.getIRI().getShortForm().equals("High_Experience_Mode")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner("High_Mode", id);
        }
      }

      // find Font_Decrease
      if (c.getIRI().getShortForm().equals("Font_Decrease")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner(
            "Font_Decrease",
            id
          );
        }
      }

      // find Font_Increase
      if (c.getIRI().getShortForm().equals("Font_Increase")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner(
            "Font_Increase",
            id
          );
        }
      }

      // find Dark_Mode
      if (c.getIRI().getShortForm().equals("Dark_Mode")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner("Dark_Mode", id);
        }
      }

      // find Contrast_Mode
      if (c.getIRI().getShortForm().equals("Contrast_Mode")) {
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);

        for (OWLNamedIndividual i : instances.getFlattened()) {
          long id = Long.parseLong(i.getIRI().getShortForm().split("#")[1]);
          System.out.println(id + " - " + c.getIRI().getShortForm());
          preferencesServiceImpl.updatePreferencesByReasoner(
            "Contrast_Mode",
            id
          );
        }
      }
    }
  }
}
