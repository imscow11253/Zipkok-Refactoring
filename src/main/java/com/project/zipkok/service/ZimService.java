package com.project.zipkok.service;

import com.project.zipkok.common.exception.RealEstateException;
import com.project.zipkok.common.exception.zim.NoUserOrRealEstate;
import com.project.zipkok.common.exception.zim.ZimBadRequestException;
import com.project.zipkok.dto.GetZimLoadResponse;
import com.project.zipkok.model.RealEstate;
import com.project.zipkok.model.User;
import com.project.zipkok.model.Zim;
import com.project.zipkok.repository.RealEstateRepository;
import com.project.zipkok.repository.UserRepository;
import com.project.zipkok.repository.ZimRepository;
import com.project.zipkok.util.jwt.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.project.zipkok.common.response.status.BaseExceptionResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZimService {

    private final ZimRepository zimRepository;
    private final UserRepository userRepository;
    private final RealEstateRepository realEstateRepository;

    public GetZimLoadResponse zimLoad(JwtUserDetails jwtUserDetail) {
        log.info("{ZimService.zimLoad}");

        User user = this.userRepository.findByUserId(jwtUserDetail.getUserId());
        List<Zim> zimList = this.zimRepository.findAllByUser(user);

        if(zimList == null){
            throw new ZimBadRequestException(FAVORITES_QUERY_FAILURE);
        }

        GetZimLoadResponse getZimLoadResponse = new GetZimLoadResponse();

        for(Zim zim : zimList){
            RealEstate realEstate = zim.getRealEstate();

            if(realEstate == null){
                throw new NoUserOrRealEstate(FAVORITES_QUERY_FAILURE);
            }

            getZimLoadResponse.addRealEstateInfo(realEstate.getRealEstateId(), realEstate.getImageUrl(), realEstate.getDeposit(), realEstate.getPrice(), realEstate.getAddress(), realEstate.getAgent(), zim.getRealEstate().getTransactionType().toString(), zim.getRealEstate().getRealEstateType().toString());
        }

        return getZimLoadResponse;
    }

    public Object zimRegister(JwtUserDetails jwtUserDetail, long realEstateId) {
        log.info("{ZimService.zimRegister}");

        User user = this.userRepository.findByUserId(jwtUserDetail.getUserId());
        RealEstate realEstate = this.realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new RealEstateException(INVALID_PROPERTY_ID));
        List<Zim> zimList = this.zimRepository.findAllByUser(user);

        if(user == null || realEstate == null){
            throw new NoUserOrRealEstate(FAVORITES_ADD_FAILURE);
        }
        for(Zim zim : zimList){
            if(zim.getRealEstate().getRealEstateId().equals(realEstateId)){
                throw new ZimBadRequestException(ALREADY_EXIST_ZIM);
            }
        }

        Zim zim = new Zim(realEstate, user);
        this.zimRepository.save(zim);

        return null;
    }

    public Object zimDelete(JwtUserDetails jwtUserDetail, Long realEstateId) {
        log.info("{ZimService.zimDelete}");

        User user = this.userRepository.findByUserId(jwtUserDetail.getUserId());
        List<Zim> zimList = this.zimRepository.findAllByUser(user);

        for(Zim zim : zimList){
            if(zim.getRealEstate().getRealEstateId().equals(realEstateId)){
                this.zimRepository.delete(zim);
                return null;
            }
        }

        throw new ZimBadRequestException(NO_EXIST_ZIM);
    }
}
