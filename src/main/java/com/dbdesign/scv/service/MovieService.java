package com.dbdesign.scv.service;

import com.dbdesign.scv.dto.*;
import com.dbdesign.scv.entity.Genre;
import com.dbdesign.scv.entity.Movie;
import com.dbdesign.scv.entity.MovieGenre;
import com.dbdesign.scv.entity.Showtime;
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

        Movie oldMovie = movieRepository.findMovieByName(movieFormDTO.getName());

        if (oldMovie != null && oldMovie.getDeleted() == 'N') { // 중복 등록 방지 && 삭제된 거면 가능
            throw new IllegalArgumentException("이미 등록된 영화입니다.");
        }

        Movie movie = Movie.builder().name(movieFormDTO.getName()).length(movieFormDTO.getLength()).rating(movieFormDTO.getRating()).director(movieFormDTO.getDirector()).introduction(movieFormDTO.getIntroduction()).distributor(movieFormDTO.getDistributor()).imgUrl(movieFormDTO.getImgUrl()).actor(movieFormDTO.getActor()).staff(movieFormDTO.getStaff()).deleted('N').build();

        movieRepository.save(movie);

        for (GenreDTO genreDTO : movieFormDTO.getGenreDTOList()) {
            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setMovie(movie);

            for (Genre genre : genreRepository.findAll()) {
                if (genre.getDeleted() == 'N' && genre.getName().equals(genreDTO.getName())) {
                    movieGenre.setGenre(genre);
                }
            }

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
        movie.setImgUrl(updateMovieDTO.getNewImgUrl());
        movie.setActor(updateMovieDTO.getNewActor());
        movie.setStaff(updateMovieDTO.getNewStaff());

        movieRepository.save(movie);

        // movie_genre 갱신 (장르 갱신)
        movieGenreRepository.deleteMovieGenresByMovie(movie);

        for (GenreDTO genreDTO : updateMovieDTO.getGenreDTOList()) {

            Genre newGenre = null;
            for (Genre genre : genreRepository.findAll()) {
                if (genre.getDeleted() == 'N' && genre.getName().equals(genreDTO.getName())) {
                    newGenre = genre;
                }
            }
            MovieGenre movieGenre = new MovieGenre();
            movieGenre.setMovie(movie);
            movieGenre.setGenre(newGenre);

            movieGenreRepository.save(movieGenre);
        }
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

        for (Genre genre : genreRepository.findAll()) {
            if (genre.getDeleted() == 'N' && genre.getName().equals(genreDTO.getName())) { // 중복 등록 방지 && 삭제된 거면 가능
                throw new IllegalArgumentException("이미 등록된 장르입니다.");
            }
        }

        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genre.setDeleted('N');

        genreRepository.save(genre);
    }

    // 장르 리스트 반환
    public List<GenreDTO> genreList() {

        List<GenreDTO> genreDTOList = new ArrayList<>();

        for (Genre genre : genreRepository.findAll()) {
            if (genre.getDeleted() == 'N') {
                GenreDTO genreDTO = new GenreDTO();
                genreDTO.setName(genre.getName());

                genreDTOList.add(genreDTO);
            }
        }
        return genreDTOList;
    }

    // 영화 장르 (논리적) 삭제
    @Transactional
    public void deleteGenre(String name) {

        List<Genre> genreList = genreRepository.findAllByName(name);

        if (genreList.size() == 0) { // 받은 name 으로 장르가 존재하는 지 확인
            throw new IllegalArgumentException("삭제할 장르가 존재하지 않습니다.");
        }

        for (MovieGenre movieGenre : movieGenreRepository.findAll()) { // 삭제되지 않은 영화 중, 삭제하려는 장르로 매칭된 영화가 존재해도 삭제 불가
            if (movieGenre.getGenre().getName().equals(name) && movieGenre.getMovie().getDeleted() == 'N') {
                throw new IllegalArgumentException("이미 해당 장르로 매칭된 영화가 존재합니다.");
            }
        }

        // 논리적 삭제
        for (Genre genre : genreList) {
            genre.setDeleted('Y');
            genreRepository.save(genre);
        }
    }

    // 영화 장르 수정 (어드민)
    @Transactional
    public void updateGenre(UpdateGenreDTO updateGenreDTO) {

        List<Genre> genreList = genreRepository.findAllByName(updateGenreDTO.getOldName());

        Genre oldGenre = null;
        for (Genre genre : genreList) {
            if (genre.getDeleted() == 'N') {
                oldGenre = genre;
            }
        }

        // 수정할 장르가 존재하지 않는 경우
        if (oldGenre == null) {
            throw new IllegalArgumentException("수정할 장르가 존재하지 않습니다.");
        }


        for (Showtime showtime : showtimeRepository.findAll()) {
            for (MovieGenre movieGenre : movieGenreRepository.findMovieGenresByMovie(showtime.getMovie())) {
                if (movieGenre.getGenre().getId().equals(oldGenre.getId())) {
                    throw new IllegalArgumentException("이미 상영 중인 영화에 포함된 장르는 수정할 수 없습니다.");
                }
            }
        }

        oldGenre.setDeleted('Y');
        genreRepository.save(oldGenre);

        Genre newGenre = new Genre();
        newGenre.setName(updateGenreDTO.getNewName());
        newGenre.setDeleted('N');
        genreRepository.save(newGenre);
    }
}
