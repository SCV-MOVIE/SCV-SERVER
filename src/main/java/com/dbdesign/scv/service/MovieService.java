package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.*;
import com.dbdesign.scv.entity.Genre;
import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.MovieGenre;
import com.dbdesign.scv.repository.GenreRepository;
import com.dbdesign.scv.repository.MovieGenreRepository;
import com.dbdesign.scv.repository.MovieRepository;
import com.dbdesign.scv.repository.ShowtimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final GenreRepository genreRepository;
    private final MovieGenreRepository movieGenreRepository;

    public MovieService(MovieRepository movieRepository, ShowtimeRepository showtimeRepository, GenreRepository genreRepository, MovieGenreRepository movieGenreRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
        this.genreRepository = genreRepository;
        this.movieGenreRepository = movieGenreRepository;
    }

    // 영화 등록
    @Transactional
    public void registerMovie(MovieFormDTO movieFormDTO) {

        Movie movie = Movie.builder()
                .name(movieFormDTO.getName())
                .length(movieFormDTO.getLength())
                .rating(movieFormDTO.getRating())
                .director(movieFormDTO.getDirector())
                .introduction(movieFormDTO.getIntroduction())
                .distributor(movieFormDTO.getDistributor())
                .imgUrl(movieFormDTO.getImgUrl())
                .actor(movieFormDTO.getActor())
                .staff(movieFormDTO.getStaff())
                .deleted('N')
                .build();

        movieRepository.save(movie);

        for (GenreDTO genreDTO : movieFormDTO.getGenreDTOList()) {
            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setMovie(movie);
            movieGenre.setGenre(genreRepository.findGenreByName(genreDTO.getName()));

            movieGenreRepository.save(movieGenre);
        }
    }

    // 영화 정보 수정
    @Transactional
    public void updateMovie(UpdateMovieDTO updateMovieDTO) {

        Movie movie = movieRepository.findMovieById(updateMovieDTO.getMovieId());

        // 수정할 영화가 존재하지 않는 경우
        if (movie == null) { // 받은 id 로 영화가 존재하는 지 확인
            throw new IllegalArgumentException("수정할 영화가 존재하지 않습니다.");
        }

        // 이미 삭제된 영화인 경우
        if (movie.getDeleted() == 'Y') {
            throw new IllegalArgumentException("삭제된 영화입니다.");
        }

        movie.setLength(updateMovieDTO.getNewLength());
        movie.setRating(updateMovieDTO.getNewRating());
        movie.setDirector(updateMovieDTO.getNewDirector());
        movie.setIntroduction(updateMovieDTO.getNewIntroduction());
        movie.setDistributor(updateMovieDTO.getNewDistributor());
        movie.setImgUrl(updateMovieDTO.getNewImageUrl());
        movie.setActor(updateMovieDTO.getNewActor());
        movie.setStaff(updateMovieDTO.getNewStaff());
        //TODO: 장르도 변경 가능하게 할 것인지

        movieRepository.save(movie);
    }

    // 영화 삭제
    @Transactional
    public void deleteMovie(DeleteMovieDTO deleteMovieDTO) {

        Movie movie = movieRepository.findMovieById(deleteMovieDTO.getMovieId());

        // 삭제할 영화가 존재하지 않는 경우
        if (movie == null) { // 받은 id 로 영화가 존재하는 지 확인
            throw new IllegalArgumentException("삭제할 영화가 존재하지 않습니다.");
        }

        // 상영 일정이 없는 영화의 경우에만 삭제가 가능
        if (showtimeRepository.existsByMovie(movie)) {
            throw new IllegalArgumentException("상영 일정이 없는 영화의 경우에만 삭제가 가능합니다.");
        }

        // 이미 삭제된 영화인 경우
        if (movie.getDeleted() == 'Y') {
            throw new IllegalArgumentException("이미 삭제된 영화입니다.");
        }

        movie.setDeleted('Y');

        movieRepository.save(movie);
    }

    // 영화 리스트 조회 (영화 장르 포함)
    public List<MovieDTO> movieList() {

        List<MovieDTO> movieDTOList = new ArrayList<>();

        for (Movie movie : movieRepository.findAll()) {
            if (movie.getDeleted() != 'Y') {
                MovieDTO movieDTO = MovieDTO.from(movie);
                List<GenreDTO> genreDTOList = new ArrayList<>();
                for (MovieGenre movieGenre : movieGenreRepository.findMovieGenresByMovie(movie)) {
                    GenreDTO genreDTO = new GenreDTO();
                    genreDTO.setName(movieGenre.getGenre().getName());

                    genreDTOList.add(genreDTO);
                }
                movieDTO.setGenreDTOList(genreDTOList);

                movieDTOList.add(movieDTO);
            }
        }

        return movieDTOList;
    }

    // 장르 등록
    @Transactional
    public void registerGenre(GenreDTO genreDTO) {

        Genre genre = new Genre();
        genre.setName(genreDTO.getName());

        genreRepository.save(genre);
    }

    public List<GenreDTO> genreList() {

        List<GenreDTO> genreDTOList = new ArrayList<>();

        for (Genre genre : genreRepository.findAll()) {
            GenreDTO genreDTO = new GenreDTO();
            genreDTO.setName(genre.getName());

            genreDTOList.add(genreDTO);
        }
        return genreDTOList;
    }
}