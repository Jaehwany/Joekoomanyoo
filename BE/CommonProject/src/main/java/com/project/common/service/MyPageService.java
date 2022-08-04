package com.project.common.service;

import com.project.common.dto.My.MyDailyMemoDto;
import com.project.common.dto.My.MyDailyMemoMapper;
import com.project.common.dto.My.MyScheduleDto;
import com.project.common.dto.My.MyScheduleMapper;
import com.project.common.dto.User.UserKeywordDto;
import com.project.common.dto.User.UserKeywordMapper;
import com.project.common.entity.My.MyDailyMemoEntity;
import com.project.common.entity.My.MyScheduleEntity;
import com.project.common.entity.User.UserKeywordEntity;
import com.project.common.repository.MyDailyMemoRepository;
import com.project.common.repository.MyDailyMemoRepositoryCustom;
import com.project.common.repository.MyScheduleRepository;
import com.project.common.repository.MyScheduleRepositoryCustom;
import com.project.common.repository.User.UserKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final UserKeywordRepository userKeywordRepository;

    private final MyDailyMemoRepositoryCustom myDailyMemoRepositoryCustom;

    private final MyDailyMemoRepository myDailyMemoRepository;
    private final MyScheduleRepository myScheduleRepository;

    private final MyScheduleRepositoryCustom myScheduleRepositoryCustom;

    public boolean createKeyword(UserKeywordDto userKeywordDto) {
        UserKeywordEntity userKeywordEntity = UserKeywordMapper.MAPPER.toEntity(userKeywordDto);

        // 만약 이미 키워드가 존재하는 경우
        if(userKeywordEntity == null){
            return false;
        }else{
            // 현재 날짜 , 인덱스 주기
            userKeywordEntity.setMyKeywordRegistedAt(LocalDateTime.now());
            userKeywordEntity.setMyKeywordSeq(0);
            userKeywordRepository.save(userKeywordEntity);
            return true;
        }
    }

    public List<UserKeywordDto> listKeyword(int userSeq) {
        List<UserKeywordDto> listDto = new ArrayList<>();

        List<UserKeywordEntity> list = userKeywordRepository.findAllByUserSeq(userSeq);
        for(UserKeywordEntity userKeywordEntity : list){
            listDto.add(UserKeywordMapper.MAPPER.toDto(userKeywordEntity));
        }

        return listDto;
    }

    public boolean deleteKeyword(int myKeywordSeq) {
        // 키워드가 없는 경우
        if(userKeywordRepository.findByMyKeywordSeq(myKeywordSeq)==null){
            return false;
        }else {
            userKeywordRepository.deleteByMyKeywordSeq(myKeywordSeq);
            return true;
        }
    }

    public boolean createDailyMemo(MyDailyMemoDto myDailyMemoDto) {
        // 이미 메모가 있으면
        if(myDailyMemoRepositoryCustom.findByUserSeqAndMyDailyMemoDate(myDailyMemoDto.getUserSeq(), myDailyMemoDto.getMyDailyMemoDate()) != null){
            return false;
        }else {
            myDailyMemoDto.setMyDailyMemoRegistedAt(LocalDateTime.now());
            myDailyMemoDto.setMyDailyMemoUpdatedAt(LocalDateTime.now());

            MyDailyMemoEntity myDailyMemoEntity = MyDailyMemoMapper.MAPPER.toEntity(myDailyMemoDto);
            myDailyMemoRepository.save(myDailyMemoEntity);
            return true;
        }
    }

    public MyDailyMemoDto showDailyMemo(MyDailyMemoDto myDailyMemoDto) {
        MyDailyMemoEntity myDailyMemoEntity = myDailyMemoRepositoryCustom.findByUserSeqAndMyDailyMemoDate(myDailyMemoDto.getUserSeq(), myDailyMemoDto.getMyDailyMemoDate());
        // 데일리 메모가 없는 경우
        if(myDailyMemoEntity == null){
            return null;
        }else{
            return MyDailyMemoMapper.MAPPER.toDto(myDailyMemoEntity);
        }
    }

    public boolean modifyDailyMemo(MyDailyMemoDto myDailyMemoDto) {
        MyDailyMemoEntity myDailyMemoEntity =myDailyMemoRepositoryCustom.findByUserSeqAndMyDailyMemoDate(myDailyMemoDto.getUserSeq(), myDailyMemoDto.getMyDailyMemoDate());
        // 메모가 없으면
        if(myDailyMemoEntity == null){
            return false;
        }else {
            // 메모 내용 수정
            myDailyMemoEntity.setMyDailyMemo(myDailyMemoDto.getMyDailyMemo());
            myDailyMemoEntity.setMyDailyMemoUpdatedAt(LocalDateTime.now());

            myDailyMemoRepository.save(myDailyMemoEntity);
            return true;
        }
    }

    public boolean deleteDailyMemo(int myDailyMemoSeq) {
        // 만약 메모가 없는 경우
        if(myDailyMemoRepository.findByMyDailyMemoSeq(myDailyMemoSeq) == null){
            return false;
        }else {
            myDailyMemoRepository.deleteByMyDailyMemoSeq(myDailyMemoSeq);
            return true;
        }
    }

    public boolean createSchedule(MyScheduleDto myScheduleDto) {
        // 이미 일정이 있는 경우 false
        if(myScheduleRepositoryCustom.findByUserSeqAndMyScheduleDateAndMyScheduleTime(myScheduleDto.getUserSeq(), myScheduleDto.getMyScheduleDate(), myScheduleDto.getMyScheduleTime())!=null){
            return false;
        }else {
            MyScheduleEntity myScheduleEntity = MyScheduleMapper.MAPPER.toEntity(myScheduleDto);
            // 시간 등록
            myScheduleEntity.setMyScheduleRegistedAt(LocalDateTime.now());
            myScheduleEntity.setMyScheduleUpdatedAt(LocalDateTime.now());

            myScheduleRepository.save(myScheduleEntity);
            return true;
        }
    }

    public List<MyScheduleDto> listSchedule(MyScheduleDto myScheduleDto) {
        List<MyScheduleEntity> list = myScheduleRepositoryCustom.findByUserSeqAndMyScheduleDate(myScheduleDto.getUserSeq(), myScheduleDto.getMyScheduleDate());
        // 리스트에 아무것도 없는 경우 ( 일정이 없다 )
        if(list.size()==0){
            return null;
        }else {
            List<MyScheduleDto> listDto = new ArrayList<>();
            for (MyScheduleEntity myScheduleEntity : list) {
                listDto.add(MyScheduleMapper.MAPPER.toDto(myScheduleEntity));
            }
            return listDto;
        }
    }

    public boolean modifySchedule(MyScheduleDto myScheduleDto) {
        // 그전 시간 값 구하기
        int beforeTime = myScheduleRepository.findByMyScheduleSeq(myScheduleDto.getMyScheduleSeq()).getMyScheduleTime();

        // 일정 체크 ( 날짜, 시간, 사용자 이용해서 )
        MyScheduleEntity myScheduleEntity = myScheduleRepositoryCustom.findByUserSeqAndMyScheduleDateAndMyScheduleTime(myScheduleDto.getUserSeq(), myScheduleDto.getMyScheduleDate(), beforeTime);
        // 일정이 없는 경우
        if(myScheduleEntity==null){
            return false;
        }else{
            // 시간 설정
            myScheduleEntity.setMyScheduleTime(myScheduleDto.getMyScheduleTime());
            // 일정 내용 설정
            myScheduleEntity.setMyScheduleContent(myScheduleDto.getMyScheduleContent());
            // 업데이트 시간 설정
            myScheduleEntity.setMyScheduleUpdatedAt(LocalDateTime.now());
            // 날짜는 변경 불가능
            
            // 저장
            myScheduleRepository.save(myScheduleEntity);
            return true;
        }
    }
}