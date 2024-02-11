package everymeal.server.review.service;


import everymeal.server.review.entity.Image;
import everymeal.server.review.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageCommServiceImpl {

    private final ImageRepository imageRepository;

    @Transactional
    public void deleteImage(Image alreadyImg) {
        imageRepository.delete(alreadyImg);
    }
}
