package pl.alekaug.quizplatform.answer;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswersService {
    @Autowired
    private AnswersRepository answersRepo;

    public Optional<Answer> getById(long id) throws IllegalArgumentException {
        return answersRepo.findById(id);
    }

    public List<Answer> getAll() {
        return answersRepo.findAll();
    }


    public Answer add(Answer answer) throws IllegalArgumentException {
        return answersRepo.save(answer);
    }

    public Answer replace(long id, Answer answer) throws IllegalArgumentException {
        Optional<Answer> a = answersRepo.findById(id);
        if (a.isPresent()) {
            Answer original = a.get();
            original.changeTo(answer);
            return answersRepo.save(original);
        }
        else
            return null;
    }

    public boolean remove(long answerId) throws IllegalArgumentException {
        if (answersRepo.existsById(answerId))
            answersRepo.deleteById(answerId);
        else
            return false;
        return true;
    }

    public Integer removeAll() {
        return answersRepo.deleteAllReturnNumber();
    }
}
