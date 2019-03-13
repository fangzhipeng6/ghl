package org.crazyit.demo.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.crazyit.demo.entity.Product;
import org.crazyit.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.crazyit.demo.DemoApp;
import org.crazyit.demo.WebMvcConfig;
import org.crazyit.demo.util.FileUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	@Autowired
    private HttpSession session;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        dateFormat.setLenient(false);
	        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	 }
	
	@GetMapping("/")
	public String index(@ModelAttribute Product product) {
		return "index";
	}

	
	@GetMapping("/product/list")
    public String list(Model model,
            @PageableDefault(size = DemoApp.PAGE_SIZE) Pageable pageable) {
        model.addAttribute("datas", productService.findAll(pageable));//往前台传数据，数据会自动添加到数据模型中，可以传对象，可以传List，通过el表达式 ${}可以获取到,JpaRepository 接口中的分页方法 findAll(pageable),当传入参数为Pageable时，返回传则是一个分页的对象Page
        System.out.println("ghl");
        return "product/list";
    }
	
	
    /**
     * 打开添加页面
     * 
     * @return
     */
	@GetMapping("/product/add")
    public String add(@ModelAttribute Product product) {
        return "product/content";
    }
    /**
     * 打开修改界面
     */
	@RequestMapping("/product/save")
    public String save(@ModelAttribute Product product) throws IOException {
        productService.save(product);
        return "redirect:/product/list"; 
    }




    /**
     * 打开查看界面
     */
    @GetMapping("/product/view/{productId}")
    public String view(@PathVariable Integer productId, Model model) {
        model.addAttribute("product", productService.findById(productId));
        model.addAttribute("readonly", true);
        return "product/content";
    }

    @RequestMapping("/product/delete/{page}")
    public String delete(@RequestParam Integer[] ids, @PathVariable Integer page) {//@RequestParam 是传递参数的
        productService.delete(ids);
        return "redirect:/product/list?page=" + page;//
    }

    @RequestMapping("/product/upload")
    public void upload(@RequestParam("cover") MultipartFile uploadFile,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        byte[] content = uploadFile.getBytes();
        // 保存文件到具体目录，此处为D:/book/upload
        String path = WebMvcConfig.FILE_DIR;
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 获取文件后缀
        String fileSuffix = FileUtil
                .getSuffix(uploadFile.getOriginalFilename());
        // 设置文件名
        File file = new File(folder.getAbsolutePath() + File.separator
                + UUID.randomUUID().toString() + fileSuffix);
        file.createNewFile();
        // 写到服务器文件
        FileUtil.writeFile(file, content);
        response.getWriter().write("/upload/" + file.getName());
    }

    /**
     * 打开选择用户界面
     * 
     * @return
     */
    @GetMapping("/product/choose")
    public String choose(Model model,
            @PageableDefault(size = DemoApp.PAGE_SIZE) Pageable pageable) {
        model.addAttribute("datas", productService.findAll(pageable));
        return "product/choose";
    }
    
    @GetMapping("/product/test")
    public String testFindAll(Model model, Pageable pageable) {
        model.addAttribute("datas", productService.testFindAll(pageable));
        return "product/test";
    }
}
