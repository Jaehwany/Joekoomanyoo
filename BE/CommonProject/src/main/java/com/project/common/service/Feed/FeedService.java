package com.project.common.service.Feed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.common.dto.Feed.FeedDto;
import com.project.common.entity.Feed.FeedEntity;
import com.project.common.entity.Feed.FeedHashtagEntity;
import com.project.common.entity.Feed.FeedLikeEntity;
import com.project.common.entity.User.UserEntity;
import com.project.common.mapper.FeedMapper;
import com.project.common.repository.Feed.FeedHashtagRepository;
import com.project.common.repository.Feed.FeedLikeRepository;
import com.project.common.repository.Feed.FeedRepository;
import com.project.common.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService{
	private final FeedRepository feedRepository;
	private final FeedHashtagRepository feedHashtagRepository;
	private final FeedLikeRepository feedLikeRepository;
	private final UserRepository userRepository;
	
	//피드 등록
	@Transactional
	public FeedDto addFeed(int userSeq,FeedDto feedDto) {
		FeedEntity saved= feedRepository.save(feedDto.toEntity());
		UserEntity user = userRepository.findByUserSeq(userSeq);
		user.addFeed(saved);
	 	userRepository.save(user);
		
		return FeedMapper.MAPPER.toDto(saved);
	}
	
	//피드 전체 조회
	public List<FeedDto> getFeedList(){
		List<FeedEntity> feedList=feedRepository.findAll();
		if(feedList==null)
			throw new IllegalArgumentException("등록한 피드가 없습니다");
		return FeedMapper.MAPPER.toDtoList(feedList);
	}
	
	//피드 보기
	public FeedDto getFeedInfo(int feedSeq) {
		FeedEntity feedInfo=feedRepository.findById(feedSeq).orElse(null);
		if(feedInfo==null)
			throw new IllegalArgumentException("등록한 피드가 없습니다");
		return FeedMapper.MAPPER.toDto(feedInfo);
	}
	
	//피드 삭제
	public void deleteFeed(int feedSeq){
		for(FeedHashtagEntity entity : feedHashtagRepository.findAll()) {
			if(entity.getFeed().getFeedSeq()==feedSeq) {
				feedHashtagRepository.deleteById(entity.getFhSeq());
			}
		}
		for(FeedLikeEntity entity : feedLikeRepository.findAll()) {
			if(entity.getFeed().getFeedSeq()==feedSeq) {
				feedLikeRepository.deleteById(entity.getFeedLikeSeq());
			}
		}
		
		feedRepository.deleteById(feedSeq);
	}
	
	//피드 수정
	public FeedDto updateFeed(int feedSeq,FeedDto feedDto) {
		FeedEntity oldFeed =feedRepository.findById(feedSeq).orElse(null);
		if(oldFeed==null)
			throw new IllegalArgumentException("등록한 피드가 없습니다");
		FeedDto newFeed=new FeedDto();
		newFeed=feedDto;
		if(oldFeed != null) {
			newFeed.setFeedSeq(oldFeed.getFeedSeq());
			newFeed.setCreatedTime(oldFeed.getCreatedTime());
			feedRepository.save(newFeed.toEntity());
		}
		return newFeed;
	}
	
	//내 피드 조회
	public List<FeedDto> getMyFeedList(int userSeq){
		List<FeedDto> feedList=new ArrayList<>();
		for(FeedEntity entity : feedRepository.findAll()) {
			if(entity.getUser().getUserSeq()==userSeq)
				feedList.add(FeedMapper.MAPPER.toDto(entity));
		}
		if(feedList.size()==0)
			throw new IllegalArgumentException("등록한 피드가 없습니다");
		return feedList;
	}
	
	//피드 공개/비공개
	public FeedDto openFeed(int feedSeq,char feedOpen) {
		FeedEntity feed =feedRepository.findById(feedSeq).orElse(null);
		feed.setFeedOpen(feedOpen);
		feedRepository.save(feed);
		return FeedMapper.MAPPER.toDto(feedRepository.save(feed));
	}

	
	public FeedEntity findFeed(int feedSeq) {
		FeedEntity findFeed = feedRepository.findById(feedSeq).orElse(null);
	    if (findFeed == null) {
	    	throw new IllegalArgumentException("해당하는 피드가 없습니다.");
		}
		 return findFeed;
	}


}
