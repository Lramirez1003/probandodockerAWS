package pucmm.edu.eitc.practica2mocky.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pucmm.edu.eitc.practica2mocky.encapsulaciones.Mock;
import pucmm.edu.eitc.practica2mocky.repo.MockRepository;

import java.util.Date;
import java.util.List;

@Service
public class MockServices {

    static MockRepository mockRepository;

    public MockServices(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public static List<Mock> buscarMockporProyecto(long id){
        return  mockRepository.findAllByIdProyecto(id);
    }

    public static Date fecha(String f ){
        int tmp = Integer.parseInt(f);
        long mlsec = System.currentTimeMillis()+(tmp*3600000);
        Date fExp = new Date(mlsec);
        return fExp;
    }
    @Transactional
    public Mock crearMock(Mock mk){
        mockRepository.save(mk);
        return mk;
    }

    @Transactional
    public void eliminarmock(long id){
        mockRepository.deleteById(id);
    }

}
