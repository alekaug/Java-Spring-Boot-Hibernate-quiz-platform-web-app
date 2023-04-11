package pl.alekaug.quizplatform.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionsService {
    @Autowired
    private SessionsRepository sessionsRepository;

    public Session register(Session session) {
        return sessionsRepository.save(session);
    }

    public Optional<Session> getById(Long id) throws IllegalArgumentException {
        return sessionsRepository.findById(id);
    }

    public List<Session> getAll() {
        return sessionsRepository.findAll();
    }

    public boolean unregister(long answerId) throws IllegalArgumentException {
        if (!sessionsRepository.existsById(answerId))
            return false;
        sessionsRepository.deleteById(answerId);
        return true;
    }

    public Integer removeAll() {
        return sessionsRepository.deleteAllReturnNumber();
    }

}
