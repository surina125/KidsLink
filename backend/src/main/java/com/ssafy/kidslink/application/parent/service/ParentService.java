package com.ssafy.kidslink.application.parent.service;

import com.ssafy.kidslink.application.child.domain.Child;
import com.ssafy.kidslink.application.child.dto.ChildDTO;
import com.ssafy.kidslink.application.child.repository.ChildRepository;
import com.ssafy.kidslink.application.kindergartenclass.domain.KindergartenClass;
import com.ssafy.kidslink.application.kindergartenclass.repository.KindergartenClassRepository;
import com.ssafy.kidslink.application.teacher.domain.Teacher;
import com.ssafy.kidslink.common.exception.PasswordMismatchException;
import com.ssafy.kidslink.application.parent.domain.Parent;
import com.ssafy.kidslink.application.parent.dto.JoinDTO;
import com.ssafy.kidslink.application.parent.repository.ParentRepository;
import com.ssafy.kidslink.common.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final KindergartenClassRepository kindergartenClassRepository;

    /**
     * TODO #1 setKindergartenId 부분 Kindergarten 이름으로 ID 찾아오기
     */
    public void joinProcess(JoinDTO joinDTO) {
        if (!joinDTO.getPassword().equals(joinDTO.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호와 비밀번호 확인이 다릅니다.");
        }

        ChildDTO childDTO = joinDTO.getChildDTO();
        Parent parent = new Parent();
        parent.setParentName(joinDTO.getName());
        parent.setParentEmail(joinDTO.getEmail());
        parent.setParentNickname(joinDTO.getNickname());
        parent.setParentTel(joinDTO.getTel());
        parent.setParentUsername(joinDTO.getUsername());
        parent.setParentPwd(joinDTO.getPassword());

        Parent savedParent = parentRepository.save(parent);

        Child child = new Child();
        if (childDTO.getGender().equals("M")) {
            child.setChildGender(Gender.M);
        } else {
            child.setChildGender(Gender.F);
        }
        child.setChildName(childDTO.getName());
        child.setChildBirth(childDTO.getBirth());

        KindergartenClass kindergartenClass =
                kindergartenClassRepository
                        .findByKindergartenKindergartenNameAndKindergartenClassName(
                                childDTO.getKindergartenName(), childDTO.getKindergartenClassName()
                        );

        // TODO #2 수정 바람 (도메인 자체를 연결)
        child.setParent(savedParent);
        child.setKindergartenClass(kindergartenClass);

        childRepository.save(child);
    }

}
