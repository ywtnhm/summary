package webmvc;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;
import spittr.Spittle;
import spittr.data.SpittleRepository;
import spittr.web.SpittleController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Created by Administrator on 2016/11/11.
 */
public class SpittleControllerTest {
    @Test
    public void shouldShowRecentSpittles() throws Exception {
        List<Spittle> expectedSpittles=createSpittleList(20);
        SpittleRepository mockRespository=mock(SpittleRepository.class);
        when(mockRespository.findSpittles(Long.MAX_VALUE,20)).thenReturn(expectedSpittles);
        SpittleController spittleController=new SpittleController(mockRespository);

        /**
         * 因为视图名和请求路径相似导致mockMvc解析失败
         * 所以要加上
         */
        MockMvc mockMvc=standaloneSetup(spittleController).setSingleView
                (new InternalResourceView("/WEB-INF/views/spittles.jsp")).build();
        mockMvc.perform(get("/spittles"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andDo(print());
    }

    /**
     * 测试查询参数
     * @throws Exception
     */
    @Test
    public void shouldShowPagedSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(50);
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        when(mockRepository.findSpittles(238900, 50))
                .thenReturn(expectedSpittles);
        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = standaloneSetup(controller)
                .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                .build();
        mockMvc.perform(get("/spittles?max=238900&count=50"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                /**
                 * 处理异常
                 */
               /* .andExpect(model().attribute("spittleList",hasItem(expectedSpittles.toArray())))*/
                .andDo(print());
    }

    /**
     * 测试路径查询
     * @throws Exception
     */
    @Test
    public void testSpittle() throws Exception {
        Spittle exceptSpittle=new Spittle("Hello",new Date());
        SpittleRepository mockRepository = mock(SpittleRepository.class);
        //when(mockRepository.findOne(12345)).thenReturn(exceptSpittle);
        when(mockRepository.findOne(12345)).thenReturn(null);
        SpittleController spittleController=new SpittleController(mockRepository);
        MockMvc mockMvc=standaloneSetup(spittleController).build();
        mockMvc.perform(get("/spittles/12345"))
                .andExpect(view().name("spittle"))
                .andExpect(model().attributeExists("spittle"))
                .andExpect(model().attribute("spittle",exceptSpittle))
                .andDo(print());
    }
    private List<Spittle> createSpittleList(int count){
        List<Spittle>spittles=new ArrayList<Spittle>();
        for (int i=0;i<count;i++){
            spittles.add(new Spittle("Spittle"+i,new Date()));
        }
        return spittles;
    }
}
