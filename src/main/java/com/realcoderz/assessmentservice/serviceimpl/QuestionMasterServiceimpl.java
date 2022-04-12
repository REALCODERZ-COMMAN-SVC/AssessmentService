/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realcoderz.assessmentservice.serviceimpl;

import com.realcoderz.assessmentservice.domain.QuestionMaster;
import com.realcoderz.assessmentservice.domain.CodingQuestionTestCases;
import com.realcoderz.assessmentservice.domain.DifficultyMaster;
import com.realcoderz.assessmentservice.domain.LanguageMaster;
import com.realcoderz.assessmentservice.domain.QuestionOptionMapping;
import com.realcoderz.assessmentservice.repository.CodingQuestionTestCaseRepository;
import com.realcoderz.assessmentservice.repository.DifficultyMasterRepository;
import com.realcoderz.assessmentservice.repository.LanguageMasterRepository;
import com.realcoderz.assessmentservice.repository.QuestionMasterRepository;
import com.realcoderz.assessmentservice.repository.QuestionTypeRepository;
import com.realcoderz.assessmentservice.service.QuestionMasterService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Prateek, Shubham Mishra
 */
@Service
public class QuestionMasterServiceimpl implements QuestionMasterService {

    static final Logger logger = LoggerFactory.getLogger(QuestionMasterServiceimpl.class);

//    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private QuestionMasterRepository questionMasterRepository;

    @Autowired
    private CodingQuestionTestCaseRepository testCaseRepo;

    @Autowired
    private QuestionTypeRepository questionTypeRepository;

    @Autowired
    private LanguageMasterRepository languageMasterRepository;

    @Autowired
    private DifficultyMasterRepository difficultyMasterRepository;

    private boolean checkTemplateColumn(Map map, Long questionTypeId) {
        boolean result = false;
        if (questionTypeId == 1 && map.containsKey("noaIndex") && map.containsKey("oaIndex") && map.containsKey("odIndex")) {
            result = true;
        } else if (questionTypeId == 2 && map.containsKey("ctIndex") && map.containsKey("tcnIndex") && map.containsKey("tciIndex") && map.containsKey("tcoIndex") && map.containsKey("eoIndex")) {
            result = true;
        }
        return result;
    }

    private String convertTimeFormat(String time) {
        String[] timeFormat = time.split(":");
        String min = timeFormat[0], sec = timeFormat[1];
        try {
            if (min.length() > 1 && min.charAt(0) == 48) {
                min = min.substring(1);
            }
            if (timeFormat[1].length() > 1 && timeFormat[1].charAt(0) == 48) {
                sec = timeFormat[1].substring(1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        if (min.equalsIgnoreCase("0") && sec.equalsIgnoreCase("0")) {
            return "error";
        }
        return min + ":" + sec;
    }

    private boolean checkTimeValidation(String time) {
        boolean result = true;
        String[] timeFormat = time.split(" ");
        String[] timeFormat1 = time.split(":");
        if (timeFormat.length == 1 && timeFormat1.length == 2) {
            result = false;
        }
        return result;
    }

    @Override
    public Map uploadQuestions(MultipartFile file, Long organizationId, Long questionTypeId) {
        Map resultMap = new HashMap<>();
        List<Map> li = new ArrayList();

        try {
            try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                Map<String, Integer> finalmp = new HashMap<>();
                Row headerRow = sheet.getRow(0);
                headerRow.forEach(cell -> {
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                        switch (cell.getStringCellValue().toLowerCase()) {
                            case "si.no":
                                finalmp.put("serialIndex", cell.getColumnIndex());
                                break;
                            case "skills":
                                finalmp.put("skillsIndex", cell.getColumnIndex());
                                break;
                            case "level":
                                finalmp.put("levelIndex", cell.getColumnIndex());
                                break;
                            case "questiondescription":
                                finalmp.put("qdIndex", cell.getColumnIndex());
                                break;
                            case "active":
                                finalmp.put("activeIndex", cell.getColumnIndex());
                                break;
                            case "time":
                                finalmp.put("timeIndex", cell.getColumnIndex());
                                break;

                            default:
                                break;
                        }
                        if (questionTypeId == 1) {
                            switch (cell.getStringCellValue().toLowerCase()) {
                                case "noofanswer":
                                    finalmp.put("noaIndex", cell.getColumnIndex());
                                    break;
                                case "optionanswer":
                                    finalmp.put("oaIndex", cell.getColumnIndex());
                                    break;
                                case "optiondesc":
                                    finalmp.put("odIndex", cell.getColumnIndex());
                                    break;
                                case "technical":
                                    finalmp.put("techIndex", cell.getColumnIndex());
                                    break;
                            }

                        } else if (questionTypeId == 2) {
                            switch (cell.getStringCellValue().toLowerCase()) {
                                case "codingtemplate":
                                    finalmp.put("ctIndex", cell.getColumnIndex());
                                    break;
                                case "testcasename":
                                    finalmp.put("tcnIndex", cell.getColumnIndex());
                                    break;
                                case "testcaseinput":
                                    finalmp.put("tciIndex", cell.getColumnIndex());
                                    break;
                                case "testcaseoutput":
                                    finalmp.put("tcoIndex", cell.getColumnIndex());
                                    break;
                                case "expectedoutput":
                                    finalmp.put("eoIndex", cell.getColumnIndex());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
                sheet.removeRow(headerRow);
                Map mp = new LinkedHashMap();
                List<Map<String, String>> optionList = new LinkedList<>();
                List<Map<String, Object>> testCases = new LinkedList<>();
                if (isSheetEmpty(sheet)) {
                    if (checkTemplateColumn(finalmp, questionTypeId)) {
                        List<LinkedCaseInsensitiveMap> skillsMasters = languageMasterRepository.getLanguagesForDropDown(organizationId);
                        List<LinkedCaseInsensitiveMap> difficultyMasters = difficultyMasterRepository.getDifficultyForDropDown(organizationId);
                        sheet.forEach((Row row) -> {
                            if (!checkIfRowIsEmpty(row)) {
                                Map mt = new LinkedHashMap();
                                Map m = new LinkedHashMap();
                                row.forEach((Cell cell) -> {
                                    boolean a = checkSequenceValue(row, finalmp.get("serialIndex"));
                                    if (a && questionTypeId == 1) {
                                        if (cell.getColumnIndex() == finalmp.get("oaIndex")) {
                                            if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " optionAnswer is mandatory field .!");
                                            } else if (!String.valueOf(printCellValue(cell)).equalsIgnoreCase("y") && !String.valueOf(printCellValue(cell)).equalsIgnoreCase("n")) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Value of option answer should be Y or N .!");

                                            } else {
                                                mt.put("optionAnswer", printCellValue(cell));
                                            }

                                        } else if (cell.getColumnIndex() == finalmp.get("odIndex")) {
                                            if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " optionDescription is mandatory field .!");
                                            } else {
                                                m.put(String.valueOf(printCellValue(cell)), String.valueOf(mt.get("optionAnswer")));
                                            }
                                        }
                                    } else if (a && questionTypeId == 2) {
                                        if (cell.getColumnIndex() == finalmp.get("tciIndex")) {
                                            if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseInput is mandatory field .!");
                                            } else {
                                                mt.put("testCaseInput", printCellValue(cell));
                                            }
                                        } else if (cell.getColumnIndex() == finalmp.get("tcoIndex")) {
                                            if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseOutput is mandatory field .!");
                                            } else {
                                                mt.put("testCaseOutput", printCellValue(cell));
                                            }
                                        } else if (cell.getColumnIndex() == finalmp.get("tcnIndex")) {
                                            if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseName is mandatory field .!");
                                            } else {
                                                mt.put("testCaseName", printCellValue(cell));
                                            }
                                        }
                                    } else {
                                        if (mp.containsKey("slno") && cell.getColumnIndex() == finalmp.get("serialIndex")) {
                                            if (!(mp.get("slno").toString().equalsIgnoreCase(String.valueOf(finalmp.get("serialIndex"))))) {
                                                if (questionTypeId == 1) {
                                                    mp.put("options_list", new LinkedList(optionList));
                                                    this.checkForAllFields(mp, finalmp);
                                                    if (!mp.containsKey("error")) {
                                                        mp.put("error", "Valid Record");
                                                    } else {
                                                        resultMap.put("error", "");
                                                    }
                                                    li.add(new LinkedHashMap(mp));
                                                    mp.clear();
                                                    optionList.clear();
                                                    mp.put("slno", String.valueOf(printCellValue(cell)));
                                                } else if (questionTypeId == 2) {
                                                    mp.put("testCases", new LinkedList(testCases));
                                                    this.checkForAllFields(mp, finalmp);
                                                    if (!mp.containsKey("error")) {
                                                        mp.put("error", "Valid Record");
                                                    } else {
                                                        resultMap.put("error", "");
                                                    }
                                                    li.add(new LinkedHashMap(mp));
                                                    mp.clear();
                                                    testCases.clear();
                                                    mp.put("slno", String.valueOf(printCellValue(cell)));
                                                }
                                            }
                                        } else {

                                            if (cell.getColumnIndex() == finalmp.get("serialIndex")) {
                                                if (printCellValue(cell) != null || cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                                                    mp.put("slno", printCellValue(cell));
                                                }
                                            } else if (cell.getColumnIndex() == finalmp.get("skillsIndex")) {
                                                mp.put("skills_desc", printCellValue(cell));
                                                if ((mp.get("skills_desc") == null) || (mp.get("skills_desc") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("skills_desc")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Language is mandatory field .!");
                                                } else {
                                                    for (LinkedCaseInsensitiveMap language : skillsMasters) {
                                                        if (language.get("name").toString().trim().equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim())) {
                                                            mp.put("skills_desc", language.get("name").toString());
                                                            mp.put("language_id", Long.parseLong(language.get("value").toString()));
                                                        }
                                                    }
                                                }
                                            } else if (cell.getColumnIndex() == finalmp.get("levelIndex")) {
                                                mp.put("difficulty_desc", printCellValue(cell));
                                                if ((mp.get("difficulty_desc") == null) || (mp.get("difficulty_desc") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("difficulty_desc")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Level is mandatory field .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else {
                                                    for (LinkedCaseInsensitiveMap difficulty : difficultyMasters) {
                                                        if (difficulty.get("name").toString().trim().equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim())) {
                                                            mp.put("difficulty_desc", difficulty.get("name").toString().trim());
                                                            mp.put("difficulty_id", Long.parseLong(difficulty.get("value").toString()));
                                                        }
                                                    }
                                                }
                                            } else if (questionTypeId == 1 && cell.getColumnIndex() == finalmp.get("noaIndex")) {
                                                if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                                                    mp.put("no_of_answer", Double.parseDouble(String.valueOf(printCellValue(cell))));
                                                    if (String.valueOf(mp.get("no_of_answer")).trim().length() == 0 || String.valueOf(mp.get("no_of_answer")) == null || String.valueOf(mp.get("no_of_answer")) == "" || String.valueOf(mp.get("no_of_answer")) == " ") {
                                                        mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " NoOfAnswer is mandatory field .!");
                                                    }
                                                } else {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " NoOfAnswer is mandatory field .!");
                                                    mp.put("class", "alert alert-danger");
                                                }
                                            } else if (cell.getColumnIndex() == finalmp.get("qdIndex")) {
                                                mp.put("question_desc", printCellValue(cell));
                                                if ((mp.get("question_desc") == null) || (mp.get("question_desc") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("question_desc")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " QuestionDesc is mandatory field .!");
                                                } else {
                                                    mp.put("question_desc", printCellValue(cell));
                                                }
                                            } else if (cell.getColumnIndex() == finalmp.get("timeIndex")) {
                                                mp.put("questionTime", printCellValue(cell));
                                                if ((mp.get("questionTime") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("questionTime")).trim()))) || (checkTimeValidation(mp.get("questionTime").toString())) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Question Time is mandatory field and should be(min:sec).!");
                                                } else {
                                                    String time = convertTimeFormat(printCellValue(cell).toString());
                                                    if (time.equalsIgnoreCase("error")) {
                                                        mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Question Time can't be Zero.!");
                                                    } else {
                                                        mp.put("questionTime", time);
                                                    }
                                                }
                                            } else if (cell.getColumnIndex() == finalmp.get("activeIndex")) {
                                                mp.put("active", String.valueOf(printCellValue(cell)).charAt(0));
                                                if ((mp.get("active") == null) || (mp.get("active") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("active")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Active is mandatory field .!");
                                                } else if (!mp.get("active").toString().equalsIgnoreCase("y") && !mp.get("active").toString().equalsIgnoreCase("n")) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Value of active should be Y or N .!");
                                                } else {
                                                    mp.put("active", printCellValue(cell));
                                                }
                                            } else if (questionTypeId == 1 && cell.getColumnIndex() == finalmp.get("oaIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " optionAnswer is mandatory field .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else if (!String.valueOf(printCellValue(cell)).equalsIgnoreCase("y") && !String.valueOf(printCellValue(cell)).equalsIgnoreCase("n")) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Value of option answer should be Y or N .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else {
                                                    mt.put("optionAnswer", printCellValue(cell));
                                                }
                                            } else if (questionTypeId == 1 && cell.getColumnIndex() == finalmp.get("techIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Technical is mandatory field .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else if (!String.valueOf(printCellValue(cell)).equalsIgnoreCase("y") && !String.valueOf(printCellValue(cell)).equalsIgnoreCase("n")) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " Value of Technical should be Y or N .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else {
                                                    mp.put("technical", printCellValue(cell));
                                                }
                                            } else if (questionTypeId == 1 && cell.getColumnIndex() == finalmp.get("odIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " optionDescription is mandatory field .!");
                                                    mp.put("class", "alert alert-danger");
                                                } else {
                                                    m.put(String.valueOf(printCellValue(cell)), String.valueOf(mt.get("optionAnswer")));
                                                }
                                            } else if (questionTypeId == 2 && cell.getColumnIndex() == finalmp.get("eoIndex")) {
                                                mp.put("expectedOutput", String.valueOf(printCellValue(cell)));
                                                if ((mp.get("expectedOutput") == null) || (mp.get("expectedOutput") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("expectedOutput")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " ExpectedOutput is mandatory field.!");
                                                } else {
                                                    mp.put("expectedOutput", String.valueOf(printCellValue(cell)));
                                                }
                                            } else if (questionTypeId == 2 && cell.getColumnIndex() == finalmp.get("ctIndex")) {
                                                mp.put("codingTemplate", String.valueOf(printCellValue(cell)));
                                                if ((mp.get("codingTemplate") == null) || (mp.get("codingTemplate") != null && ("".equalsIgnoreCase(String.valueOf(mp.get("codingTemplate")).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " CodingTemplate is mandatory field");
                                                } else {
                                                    mp.put("codingTemplate", String.valueOf(printCellValue(cell)));
                                                }
                                            } else if (questionTypeId == 2 && cell.getColumnIndex() == finalmp.get("tcnIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseName is mandatory field");
                                                } else {
                                                    mt.put("testCaseName", String.valueOf(printCellValue(cell)));
                                                }
                                            } else if (questionTypeId == 2 && cell.getColumnIndex() == finalmp.get("tciIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseInput is mandatory field");
                                                } else {
                                                    mt.put("testCaseInput", String.valueOf(printCellValue(cell)));
                                                }
                                            } else if (questionTypeId == 2 && cell.getColumnIndex() == finalmp.get("tcoIndex")) {
                                                if ((printCellValue(cell) == null) || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
                                                    mp.put("error", (mp.get("error") != null ? mp.get("error") : "") + " TestCaseOutput is mandatory field");
                                                } else {
                                                    mt.put("testCaseOutput", String.valueOf(printCellValue(cell)));
                                                }
                                            }
                                        }
                                        if ((questionTypeId == 1 && row.getPhysicalNumberOfCells() != 10) || (questionTypeId == 2 && row.getPhysicalNumberOfCells() != 12)) {
                                            mp.put("error", (mp.get("error") != null ? (!String.valueOf(mp.get("error")).contains("Please fill all data!") ? String.valueOf(mp.get("error")) : "") : "") + " Please fill all data!");
                                        }

                                    }
                                });
                                if (questionTypeId == 1) {
                                    optionList.add(m);
                                } else if (questionTypeId == 2) {
                                    testCases.add(mt);
                                }
                                mp.put("question_type_id", questionTypeId);
                            }
                        });
                    } else {
                        resultMap.clear();
                        resultMap.put("enableSaveButton", false);
                        resultMap.put("status", "error");
                        resultMap.put("msg", "Wrong template selected");
                        return resultMap;
                    }
                }//sheet if close
                else {
                    //do nothing
                    resultMap.clear();
                    resultMap.put("enableSaveButton", false);
                    resultMap.put("list", li);
                    resultMap.put("error", "");
                    resultMap.put("empty", true);
                    return resultMap;
                }
                if (questionTypeId == 1) {
                    mp.put("options_list", optionList);
                } else if (questionTypeId == 2) {
                    mp.put("testCases", testCases);
                }

                if (!mp.containsKey("error")) {
                    mp.put("error", "Valid Record");
                    mp.put("class", "alert alert-success");
                } else {
                    resultMap.put("error", "");
                }
                li.add(mp);
            }
        } catch (Exception ex) {
            logger.error("Problem in QuestionMasterServiceImpl -> uploadQuestions() :: ", ex);
            resultMap.clear();
            resultMap.put("exception", "exception occured while uploading the excel!");
        }
        if (resultMap.containsKey("error")) {
            resultMap.put("enableSaveButton", false);
        } else {
            if (!li.isEmpty()) {
                resultMap.put("enableSaveButton", true);
            }
        }
        resultMap.put("status", "success");
        resultMap.remove("error");
        resultMap.put("list", li);
        resultMap.put("empty", false);
        return resultMap;
    }

    private void checkForAllFields(Map data, Map<String, Integer> finalmp) {
        finalmp.entrySet().forEach(entry -> {
            switch (entry.getKey().toLowerCase()) {
                case "serialindex":
                    if (!data.containsKey("slno")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " Sl.No. mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;
                case "skillsIndex":
                    if (!data.containsKey("skills_desc")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " Language is mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;

                case "levelindex":
                    if (!data.containsKey("difficulty_desc")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " Level is mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;

                case "noaindex":
                    if (!data.containsKey("no_of_answer")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " NoOfAnswer is mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;
                case "qdindex":
                    if (!data.containsKey("question_desc")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " QuestionDesc is mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;
                case "activeindex":
                    if (!data.containsKey("active")) {
                        data.put("error", (data.get("error") != null ? data.get("error") : "") + " Active is mandatory field .!");
                        data.put("class", "alert alert-danger");
                    }
                    break;

            }
        });
    }

    /**
     * To print the appropriate value according to excel column type
     *
     * @param cell - each excel cell
     * @return Object
     */
    private static Object printCellValue(Cell cell) {
        DataFormatter fmt = new DataFormatter();
        fmt.addFormat("m/d/yy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
        fmt.addFormat("m/d/yyyy", new java.text.SimpleDateFormat("dd/MM/yyyy"));
        Object obj = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                obj = fmt.formatCellValue(cell);
                break;
            case Cell.CELL_TYPE_STRING:
                obj = fmt.formatCellValue(cell);
                break;
            case Cell.CELL_TYPE_BLANK:
                obj = " ";
                break;
            case Cell.CELL_TYPE_ERROR:
                obj = cell.getErrorCellValue();
                break;

            case Cell.CELL_TYPE_FORMULA:
                obj = "";
                break;
            default:
                obj = " ";
        }

        return obj;
    }

    /**
     * To save the excel rows having only valid records
     *
     * @param mp
     * @return Integer- according to DB transaction
     */
    @Override
    public Integer excelSave(Map mp) {
        Set<QuestionMaster> questionSet = new LinkedHashSet<>();
        List<LinkedHashMap> data = (List) mp.get("data");
        Set<LanguageMaster> languageMasters = new LinkedHashSet<>();

        Set<DifficultyMaster> difficultyMasters = new LinkedHashSet<>();
        data.forEach(a -> {
            if (!a.containsKey("language_id")) {
                if (a.get("question_type_id").toString().equalsIgnoreCase("2")) {
                    languageMasters.add(new LanguageMaster(a.get("skills_desc").toString().trim(), a.get("skills_desc").toString().trim(), Long.parseLong(mp.get("organizationId").toString()), true, 'Y'));

                } else {
                    languageMasters.add(new LanguageMaster(a.get("skills_desc").toString().trim(), a.get("skills_desc").toString().trim(), Long.parseLong(mp.get("organizationId").toString()), (a.containsKey("technical") && a.get("technical") != null && a.get("technical").toString().equalsIgnoreCase("y")), 'Y'));

                }
            }
            if (a.containsKey("language_id") && a.get("language_id") != null) {
                if (a.get("question_type_id").toString().equalsIgnoreCase("2")) {
                    languageMasters.add(new LanguageMaster(Long.parseLong(a.get("language_id").toString()), a.get("skills_desc").toString(), a.get("skills_desc").toString(), Long.parseLong(mp.get("organizationId").toString()), true, 'Y'));

                } else {
                    languageMasters.add(new LanguageMaster(Long.parseLong(a.get("language_id").toString()), a.get("skills_desc").toString(), a.get("skills_desc").toString(), Long.parseLong(mp.get("organizationId").toString()), (a.containsKey("technical") && a.get("technical") != null && a.get("technical").toString().equalsIgnoreCase("y")), 'Y'));

                }
            }
            if (!a.containsKey("difficulty_id")) {
                difficultyMasters.add(new DifficultyMaster(a.get("difficulty_desc").toString().trim(), a.get("difficulty_desc").toString().trim(), 'Y', Long.parseLong(mp.get("organizationId").toString())));
            }
        });
        languageMasterRepository.saveAll(languageMasters);

        difficultyMasterRepository.saveAll(difficultyMasters);
        data.forEach(a -> {
            if (!a.containsKey("language_id")) {
                a.put("language_id", (languageMasters.stream().filter(l -> l.getLanguage_name().equalsIgnoreCase(a.get("skills_desc").toString().trim())).findFirst()).get().getLanguage_id());
            }

            if (!a.containsKey("difficulty_id")) {
                a.put("difficulty_id", (difficultyMasters.stream().filter(d -> d.getDifficulty_name().equalsIgnoreCase(a.get("difficulty_desc").toString().trim())).findFirst()).get().getDifficulty_id());
            }
            QuestionMaster questionMaster = new QuestionMaster();
            questionMaster.setLanguage_id(Long.parseLong(a.get("language_id").toString()));
            questionMaster.setDifficulty_id(Long.parseLong(a.get("difficulty_id").toString()));
            questionMaster.setQuestion_type_id(Long.parseLong(a.get("question_type_id").toString()));
            questionMaster.setQuestion_desc("<pre>" + a.get("question_desc") + "</pre>");
            questionMaster.setQuestionTime(a.get("questionTime").toString());
            questionMaster.setActive(a.get("active").toString().charAt(0));
            if (a.get("question_type_id").toString().equalsIgnoreCase("1")) {
                questionMaster.setNo_of_answer(Integer.parseInt(a.get("no_of_answer").toString()));
                List<LinkedHashMap> list = (List<LinkedHashMap>) a.get("options_list");
                List<QuestionOptionMapping> options = new ArrayList<>();
                list.stream().forEach(qom -> {
                    qom.keySet().stream().forEach(option -> {
                        QuestionOptionMapping questionOptionMapping = new QuestionOptionMapping();
                        questionOptionMapping.setOption_desc(option.toString());
                        questionOptionMapping.setIsActive(qom.get(option.toString()).toString().charAt(0));
                        questionOptionMapping.setQuestionMaster(questionMaster);
                        options.add(questionOptionMapping);
                    });
                });
                questionMaster.setOptions_list(options);
            } else if (a.get("question_type_id").toString().equalsIgnoreCase("2")) {
                questionMaster.setCodingTemplate("<pre>" + a.get("codingTemplate").toString() + "</pre>");
                questionMaster.setExpectedOutput(a.get("expectedOutput").toString());
                List<LinkedHashMap> list = (List<LinkedHashMap>) a.get("testCases");
                List<CodingQuestionTestCases> testCases =  new ArrayList<>();
                list.stream().forEach(li -> {
                    CodingQuestionTestCases testcase = new CodingQuestionTestCases();
                    testcase.setTestCaseName(li.get("testCaseName").toString());
                    testcase.setInput(li.get("testCaseInput").toString());
                    testcase.setExpectedOutput(li.get("testCaseOutput").toString());
                    testcase.setQuestionMaster(questionMaster);
                    testCases.add(testcase);
                });
                questionMaster.setTestCases(testCases);
            }
            questionMaster.setOrganizationId(Long.parseLong(mp.get("organizationId").toString()));
            questionSet.add(questionMaster);
        });
        questionMasterRepository.saveAll(questionSet);
        return questionSet.size();
    }

    /**
     * To save the excel rows having only valid records
     *
     * @param mp
     * @return Integer- according to DB transaction
     */
    private boolean checkSequenceValue(Row row, int index) {
        boolean result = false;
        try {
            if (printCellValue(row.getCell(index)).toString().equalsIgnoreCase(" ")) {//|| row.getCell(index).getCellType() == Cell.CELL_TYPE_BLANK
                result = true;
            }
        } catch (NullPointerException ex) {
            return true;
        }
        return result;
    }

    @Override
    public Boolean isAlreadyExist(QuestionMaster question) {
        if (question.getQuestion_id() == null) {
            return questionMasterRepository.questionHavingSameDesc(question.getQuestion_desc(), question.getLanguage_id(), question.getDifficulty_id()) > 0;
        } else {
            return questionMasterRepository.checkQuestionHavingSameDescWithQuestionId(question.getQuestion_desc(), question.getQuestion_id(), question.getLanguage_id(), question.getDifficulty_id()) > 0;
        }
    }

    //to check if row is empty 
    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    //to check if sheet is empty
    boolean isSheetEmpty(Sheet sheet) {
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            Row row = (Row) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = (Cell) cells.next();
                if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
                    return true;
                }
            }
        }
        return false;
    }

    //to check if cell is empty
    boolean isCellEmpty(Cell cell) {
        boolean result = false;
        if (printCellValue(cell) == null || (printCellValue(cell) != null && ("".equalsIgnoreCase(String.valueOf(printCellValue(cell)).trim()))) || (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
            result = true;
        }
        return result;
    }

    @Override
    public LinkedCaseInsensitiveMap getCodingQues(long id) {
        LinkedCaseInsensitiveMap returnMap = questionMasterRepository.getCodingQues(id);
        if (returnMap != null && !returnMap.isEmpty() && returnMap.containsKey("question_id")) {
            List<CodingQuestionTestCases> testCaseList = testCaseRepo.findByQuestionId(Long.parseLong(returnMap.get("question_id").toString()));
            returnMap.put("testCases", testCaseList);
        }
        return returnMap;
    }

    @Override
    public LinkedCaseInsensitiveMap getMcqQues(long id) {
        List<LinkedCaseInsensitiveMap> list = questionMasterRepository.getMcqQues(id);
        List<LinkedCaseInsensitiveMap> optionList = new ArrayList<>();
        list.stream().forEach(value -> {
            LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap();
            map.put("option_id", value.get("option_id"));
            map.put("option_desc", value.get("option_desc"));
            map.put("isActive", value.get("isActive"));
            optionList.add(map);
            value.remove("option_id");
            value.remove("option_desc");
            value.remove("isActive");
        });
        LinkedCaseInsensitiveMap returnMap = list.get(0);
        returnMap.put("options_list", optionList);
        return returnMap;
    }

    @Override
    public void saveQuestion(QuestionMaster questionMaster) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LinkedCaseInsensitiveMap> questions(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Optional<QuestionMaster> findById(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Long id, QuestionMaster updatedQuestion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//
//    @Override
//    public Boolean isAlreadyExist(QuestionMaster question) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
