/* Controllers */
app.controller('AnswerCtrl',  ['ENV','$scope', '$modal','$state','$stateParams',function(ENV,$scope,$modal, $state ,$stateParams) {
    $scope.host = ENV.api;
    var api_add_data= $scope.host+"wx/addExam";
    var cur_index = 0;
    var scores = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
    var rightAnswer = ["A","B","A","C","D","D","B","C","D","B","B","B","D","C","A","A","C","A","C","C",];
    var title = ["1、中国计划生育协会是中国共产党领导的（ ），是党和政府联系广大育龄群众和计划生育家庭的桥梁和纽带，是协助政府落实计划生育基本国策、促进人口长期均衡发展与家庭和谐幸福的重要力量。",
        "2.中国计划生育协会坚持创新、协调、绿色、开放、共享的发展理念,按照中央关于加强和改进党的群团工作的战略部署，保持和增强（ ），发挥带头、宣传、服务、监督、交流的作用。",
        "3.计生协会的“六项重点任务”是（ ）：",
        "4.计生协会要维护育龄群众和计划生育家庭合法权益，倾听群众意见，反映群众诉求，促进社会公平正义。推动计划生育基层群众自治，动员、引导会员和群众实行（ ）。",
        "5.计划生育协会会员分为个人会员和团体会员。个人会员、团体会员享有同等权利，对会员权利说法不正确的是（ ）：",
        "6.计生协会的个人会员、团体会员履行同等义务，其义务说法不正确的是（ ）：",
        "7.关于志愿者和志愿者组织，以下说法正确的是（ ）：",
        "8.计划生育协会实行民主集中制，其领导机构是（ ）：",
        "9.全国会员代表大会每届任期（ ），会议每五年召开一次，由全国理事会召集，并由大会预备会议选举的主席团主持。如遇特殊情况，可提前或延期召开，时间不超过一年。",
        "10.地方各级计划生育协会机关为同级计划生育协会的执行机构。县级以上（含县级）计划生育协会机关工作人员属于（ ）：",
        "11.城乡社区和村（居）计划生育协会，企业事业单位和流动人口计划生育协会，是中国计划生育协会的基层组织。基层计划生育协会会员大会或会员代表大会每（ ）召开一次，如遇特殊情况可提前或延期召开，时间不超过一年。",
        "12.关于计划生育协会各项经费的有关说法不正确的是（ ）：",
        "13、依据《福建省人口与计划生育条例》，下列哪种情形，经批准可以再生育一个子女。（   ）",
        "14、《福建省人口与计划生育条例》规定，在计划生育工作中做出显著成绩的组织和个人，由地方各级人民政府给予奖励。累计（   ）次被评为县级以上计划生育先进工作者的，享受同级劳动模范待遇",
        "15、《中华人民共和国人口与计划生育法》规定，不符合本法规定生育子女的公民，应当依法缴纳（  ）。",
        "16、《中华人民共和国人口与计划生育法》规定，实行计划生育，以（  ）为主。",
        "17、国家提倡一对夫妻生育（  ）子女。",
        "18、《福建省人口与计划生育条例》规定，在落实节育措施休假期间，或者因计划生育手术引起的并发症治疗期间，或者施行吻合手术期间，机关、企业事业单位工作人员工资照发，不影响晋升。以上说法是（ ）：",
        "19、《福建省人口与计划生育条例》规定，在国家提倡一对夫妻生育一个子女期间，农村领取独生子女父母光荣证并已绝育的家庭，在审核宅基地、村级集体经济收益分红等利益分配时，独生子女按（   ）人计算。",
        "20、符合《福建省人口与计划生育条例》生育子女的夫妻，女方产假延长为一百五十八日至一百八十日，男方照顾假为(  )。婚假、产假、照顾假期间，工资照发，不影响晋升。",

    ];
    var sA =[
        "A.全国性群团组织",
        "A.机关性、群团性、公益性",
        "A.宣传教育、生殖健康咨询服务、优生优育指导、计划生育家庭帮扶、权益维护和流动人口服务",
        "A.自我认知、自我教育、自我激励、自我控制",
        "A.优先享受计划生育协会的帮扶救助和相关服务，接受表彰和奖励",
        "A.遵守本章程，执行计划生育协会的决议",
        "A. 国内社会组织只需向所在地县以上计划生育协会提出书面备案，无需经批准，即可成为计划生育协会的志愿者组织。",
        "A.上级计划生育协会。",
        "A.二年",
        "A.公务员",
        "A.二年",
        "A.列入同级财政预算予以保证",
        "A.一对夫妻已有两个子女的，但两个子女中有一个是残疾儿。",
        "A.一",
        "A.社会抚养费",
        "A.避孕",
        "A.无限制",
        "A.对",
        "A.1",
        "A.7日",
    ];
    var sB =[
        "B.盈利性社团组织",
        "B.政治性、先进性、群众性",
        "B宣传倡导、帮扶群众、引导群众、教育群众、管理群众、监督群众",
        "B.自我管理、自我监督、自我服务、自我提升 ",
        "B.参加计划生育协会会议、培训等活动",
        "B.参加计划生育协会的活动，促进计划生育协会任务的完成",
        "B. 积极参与计划生育协会志愿服务活动，并注意保护服务对象的隐私和合法权益。",
        "B.同级计划生育协会机关。",
        "B.三年",
        "B.参照公务员法管理人员",
        "B.三年",
        "B. 不可以接受国内外组织、机构、个人捐赠",
        "B.再婚夫妻再婚前一方已有两个子女，另一方也有一个子女。",
        "B.二",
        "B.生育费",
        "B.自觉",
        "B.一个",
        "B.错",
        "B.1.5",
        "B.10日",
    ];
    var sC =[
        "C.党政机关",
        "C.公益性、社会性、群众性",
        "C促进人口法实施、计生综合管理、增强群众婚育观念、弘扬先进人口文化、实施生育关怀、强化队伍建设",
        "C.自我管理、自我教育、自我服务、自我监督",
        "C.有选举权、被选举权和表决权",
        "C.主动联系身边的群众，反映群众的意见和要求",
        "C.在计划生育协会范围内，志愿者享有表决权、选举权和被选举权",
        "C.同级会员代表大会和它选举产生的理事会。",
        "C.四年",
        "C.事业单位人员 ",
        "C.四年",
        "C.会员缴纳的会费",
        "C.再婚夫妻再婚前一方有一个子女，另一方也一个子女。",
        "C.三",
        "C.扶养费",
        "C.节育措施",
        "C.两个",
        "",
        "C.2",
        "C.15日",
    ];
    var sD =[
        "D.公益性社会组织",
        "D.先进性、公益性、群众性",
        "D幸福工程、计生小额贴息贷款帮扶、安居补助、金秋助学、计生家庭意外伤害保险、慰藉失独家庭",
        "D.自我教育、自我激励、自我管理、自我关爱",
        "D.没有退会的自由",
        "D.不需要缴纳会费 ",
        "D.计划生育协会对志愿者开展计生协会志愿服务活动，由其自行负责，无须提供必要的条件和保障。",
        "D.同级政府卫生计生行政部门。",
        "D.五年",
        "D.社工",
        "D.五年",
        "D.可以建立生育关怀基金、人口福利基金等筹资平台，拓宽筹资渠道",
        "D.夫妻双方均为独生子女的少数民族农村居民的。",
        "D.四",
        "D.罚款",
        "D.强制措施",
        "D.三个 ",
        "",
        "D.3",
        "D.30日",
    ];

    $scope.answer = {
        title:"",
        A:"",
        B:"",
        C:"",
        D:""
    };
    $scope.exam={
        user_name:$stateParams.name,
        phone:$stateParams.phone,
        area:$stateParams.area,
        score:0
    }
    initData();
    function initData() {
        showLog("AnswerCtrl init");
        $scope.answer.title = title[0];
        $scope.answer.A = sA[0];
        $scope.answer.B = sB[0];
        $scope.answer.C = sC[0];
        $scope.answer.D = sD[0];
        add();
    }
    $scope.next = function () {
        var select=false;
        var radio = document.getElementsByName("answer");
        for (i=0; i<radio.length; i++) {
            if (radio[i].checked) {
                select = true
               // alert(radio[i].value+","+rightAnswer[cur_index]);
                if(rightAnswer[cur_index]==radio[i].value){
                    scores[cur_index]=5;
                   // alert("答案正确,当前总分："+total());
                }
                else{
                    scores[cur_index]=0;
                   // alert("答案错误,当前总分："+total());
                }
                radio[i].checked = false;
                cur_index++;
                if(cur_index==17){
                    $("#sc").addClass("hide");
                    $("#sd").addClass("hide");
                }
                else{
                    $("#sc").removeClass("hide");
                    $("#sd").removeClass("hide");
                }
                $scope.answer.title = title[cur_index];
                $scope.answer.A = sA[cur_index];
                $scope.answer.B = sB[cur_index];
                $scope.answer.C = sC[cur_index];
                $scope.answer.D = sD[cur_index];
            }
        }
        if(!select){
            showMsg("请选择答案")
        }
        else{
            if(cur_index==20){
                add();
                alert("感谢您的参与，你的分数是："+total());
                $state.go("access.exam");
            }
        }
    }
    $scope.previous = function () {
        if(cur_index==0){
            showMsg("已经是第一题");
            return;
        }
        var radio = document.getElementsByName("answer");
        for (i=0; i<radio.length; i++) {
            radio[i].checked = false;
        }
        cur_index--;
        if(cur_index==17){
            $("#sc").addClass("hide");
            $("#sd").addClass("hide");
        }
        else{
            $("#sc").removeClass("hide");
            $("#sd").removeClass("hide");
        }
        $scope.answer.title = title[cur_index];
        $scope.answer.A = sA[cur_index];
        $scope.answer.B = sB[cur_index];
        $scope.answer.C = sC[cur_index];
        $scope.answer.D = sD[cur_index];
    }
    function total() {
        var total = 0;
        for(var i = 0;i<scores.length;i++){
            total = total+scores[i];
        }
        return total;
    }
    function add() {
        $scope.exam.score = total();
        var data=$scope.exam;
        myHttp(
            api_add_data,
            data,
            function (obj) {
               showMsg("答题完毕")
            }
        );
    }
  }])
;