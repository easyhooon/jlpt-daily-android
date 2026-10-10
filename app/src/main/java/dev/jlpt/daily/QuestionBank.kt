package dev.jlpt.daily

import java.time.LocalDate

/** 자체 제작 학습 문항. JLPT 공식 기출 또는 공식 난이도 판정 자료가 아닙니다. */
object QuestionBank {
    val all: List<Question> = listOf(
        Question(
            id = "g01", lessonId = "lesson_g01", kind = "grammar", title = "상태가 변하기 전에",
            prompt = "「冷める前に、温かい間に」という意味になるように選んでください。\nスープが温かい（　）、飲んでください。",
            options = listOf("ために", "うちに", "ことに", "ように"), correctIndex = 1,
            explanation = "정답: うちに. 형용사 보통형 + うちに는 ‘그 상태가 유지되는 동안에’를 뜻합니다. 식기 전에 따뜻할 때 마시라는 문맥입니다. ために는 목적·원인, ように는 목표·양태를 나타내며 이 문장과 맞지 않습니다. ことに도 여기서 시간 표현이 되지 않습니다.",
        ),
        Question(
            id = "g02", lessonId = "lesson_g02", kind = "grammar", title = "가능해지는 것을 목표로",
            prompt = "「理解できることを目標に」という意味になるように選んでください。\n日本語のニュースが理解できる（　）、毎日勉強しています。",
            options = listOf("ところに", "ことに", "ように", "ばかりに"), correctIndex = 2,
            explanation = "정답: ように. 가능형 + ように는 ‘~할 수 있도록’이라는 목표를 나타냅니다. 理解できる는 이해할 수 있다는 뜻입니다. ところに는 상황에 다른 일이 끼어드는 표현, ばかりには 주로 좋지 않은 결과의 원인이므로 여기에는 맞지 않습니다. ことに는 목적을 나타내지 않습니다.",
        ),
        Question(
            id = "g03", lessonId = "lesson_g03", kind = "grammar", title = "의무",
            prompt = "「明日までに必ず出す必要がある」という意味になるように選んでください。\nこの書類は、明日までに出さ（　）。",
            options = listOf("なくてもいい", "ないほうがいい", "ないでほしい", "なければならない"), correctIndex = 3,
            explanation = "정답: なければならない. 동사 ない형에서 ない를 빼고 なければならない를 붙이면 ‘~해야 한다’라는 의무가 됩니다. 出さなくてもいい는 제출하지 않아도 됨, 出さないほうがいい는 제출하지 않는 편이 좋음, 出さないでほしい는 제출하지 말아 달라는 뜻이므로 必ず出す必要がある와 다릅니다.",
        ),
        Question(
            id = "g04", lessonId = "lesson_g04", kind = "grammar", title = "습관·능력의 변화",
            prompt = "前は漢字が読めませんでしたが、練習して、今は読める（　）なりました。",
            options = listOf("ように", "ために", "ことを", "ところで"), correctIndex = 0,
            explanation = "정답: ように. 가능형 + ようになる는 전에는 못 하던 일을 할 수 있게 된 변화를 나타냅니다. 読めるようになりました는 ‘읽을 수 있게 되었습니다’입니다. 나머지 보기는 読める와 なりました를 이 뜻으로 연결할 수 없습니다.",
        ),
        Question(
            id = "g05", lessonId = "lesson_g05", kind = "grammar", title = "준비해 두기",
            prompt = "明日の朝は忙しいので、今夜のうちにかばんに教科書を入れ（　）おきます。",
            options = listOf("た", "て", "る", "ない"), correctIndex = 1,
            explanation = "정답: て. 동사 て형 + おく는 앞으로의 일을 위해 미리 행동해 두는 표현입니다. 入れる의 て형은 入れて이므로 入れておきます가 됩니다. 내일 아침이 바쁘므로 오늘 밤에 교과서를 넣어 둔다는 뜻입니다. た·る·ない를 넣으면 이 문법의 접속이 성립하지 않습니다.",
        ),
        Question(
            id = "g06", lessonId = "lesson_g06", kind = "grammar", title = "금지",
            prompt = "「ここで写真を撮ることは禁止です」という意味になるように選んでください。\nここでは写真を撮って（　）。",
            options = listOf("もいいです", "ほしいです", "はいけません", "みたいです"), correctIndex = 2,
            explanation = "정답: はいけません. てはいけない는 ‘~해서는 안 된다’라는 금지입니다. てもいい는 허가, てほしい는 바람, てみたい는 시험 삼아 해 보고 싶은 마음을 나타내므로 금지와 다릅니다.",
        ),
        Question(
            id = "g07", lessonId = "lesson_g07", kind = "grammar", title = "결정된 일정",
            prompt = "会社の決まりでは、会議は毎週月曜日に行う（　）なっています。",
            options = listOf("ようを", "ためを", "ところに", "ことに"), correctIndex = 3,
            explanation = "정답: ことに. 동사 사전형 + ことになっている는 규칙·일정으로 이미 정해져 있는 일을 나타냅니다. 회사 규칙상 매주 월요일 회의를 하기로 되어 있다는 뜻입니다. 나머지 보기는 주어진 형태로 문장이 성립하지 않습니다.",
        ),
        Question(
            id = "g08", lessonId = "lesson_g08", kind = "grammar", title = "동작을 동시에",
            prompt = "音楽を聞き（　）、部屋を掃除しています。",
            options = listOf("ながら", "ので", "のに", "ため"), correctIndex = 0,
            explanation = "정답: ながら. 동사 ます형 어간 + ながら는 같은 사람이 두 동작을 동시에 하는 것을 나타냅니다. 聞きながら는 음악을 들으면서라는 뜻입니다. ので·のに·ため는 이 문장에서 聞き라는 어간에 직접 붙일 수 없습니다.",
        ),
        Question(
            id = "g09", lessonId = "lesson_g09", kind = "grammar", title = "경험",
            prompt = "「以前、一度経験した」という意味になるように選んでください。\n私は一度、富士山に登った（　）。",
            options = listOf("ほうがいいです", "ことがあります", "つもりです", "ところです"), correctIndex = 1,
            explanation = "정답: ことがあります. 동사 た형 + ことがある는 과거에 해 본 경험을 나타냅니다. 登ったことがあります는 올라가 본 적이 있다는 뜻입니다. たほうがいい는 조언, たところだ는 막 끝난 동작이므로 ‘이전에 한 번 경험했다’와 다릅니다. 登ったつもりです는 올라갔다고 생각한다는 뜻이므로 실제 과거 경험을 나타내는 표현과 다릅니다.",
        ),
        Question(
            id = "g10", lessonId = "lesson_g10", kind = "grammar", title = "예상과 다른 결과",
            prompt = "「たくさん勉強したにもかかわらず」という意味になるように選んでください。\nたくさん勉強した（　）、試験では思ったほど点が取れませんでした。",
            options = listOf("ので", "から", "のに", "ために"), correctIndex = 2,
            explanation = "정답: のに. 보통형 + のに는 앞의 사실에서 예상되는 결과와 다른 결과를 나타내며 아쉬움도 표현할 수 있습니다. 많이 공부했는데도 기대만큼 점수를 얻지 못했습니다. ので·から·ためには 원인을 나타내므로 많이 공부했다는 사실을 낮은 점수의 원인으로 설명하는 문맥에는 맞지 않습니다.",
        ),
        Question(
            id = "r01", lessonId = "lesson_r01", kind = "reading", title = "도서관 공지 · 날짜와 조건",
            passage = "図書館からのお知らせ\n来週の月曜日は、本棚の整理のため休館します。火曜日からはいつもどおり午前９時に開館します。休館日も、入口の横にある返却箱に本を返せます。ただし、ＣＤは返却箱に入れないでください。ＣＤは開館日にカウンターで返してください。",
            prompt = "来週の月曜日にできることはどれですか。",
            options = listOf("午前９時に館内で本を読む。", "返却箱にＣＤを返す。", "カウンターでＣＤを返す。", "返却箱に本を返す。"), correctIndex = 3,
            explanation = "정답: 返却箱に本を返す. 월요일은 휴관이지만 반납함에 책은 반납할 수 있다고 명시했습니다. CD는 반납함에 넣으면 안 되고 개관일에 카운터에서 반납해야 합니다. 화요일 오전 9시부터 정상 개관이므로 월요일에 관내 이용이나 카운터 반납을 할 수 있다는 근거는 없습니다.",
        ),
        Question(
            id = "r02", lessonId = "lesson_r02", kind = "reading", title = "약속 메시지 · 변경된 만남",
            passage = "田中さんへ\n土曜日の映画は午後２時に始まります。初めは１時半に駅で会う予定でしたが、私は午前中に用事ができました。駅には寄らず、１時４５分に映画館の入口に直接行きます。チケットはもう２枚買ってあります。先に着いたら、入口で待っていてください。\n山田",
            prompt = "田中さんは、何時にどこで山田さんと会いますか。",
            options = listOf("午後１時４５分に映画館の入口で。", "午後１時半に駅で。", "午後２時に駅で。", "午後１時４５分に映画館の中で。"), correctIndex = 0,
            explanation = "정답: 오후 1시 45분, 영화관 입구. 원래 약속은 1시 반 역이었지만 야마다가 오전에 볼일이 생겨 역에 들르지 않고 영화관 입구로 바로 온다고 변경했습니다. 오후 2시는 영화 시작 시간입니다. 入口는 입구이므로 영화관 안이라는 보기도 틀립니다.",
        ),
        Question(
            id = "r03", lessonId = "lesson_r03", kind = "reading", title = "공부 방식 · 선택의 이유",
            passage = "私は仕事のあとで日本語を勉強しています。以前は一日に一時間勉強する計画を立てましたが、疲れた日は何もしませんでした。今は、毎日１０分だけでも勉強することにしています。短いので始めやすく、時間がある日はそのあとも続けます。長い時間を決めるより、毎日続けることが私には大切だと思います。",
            prompt = "筆者が今の勉強方法を選んだ理由は何ですか。",
            options = listOf("毎日必ず一時間勉強できるから。", "短い時間なので始めやすく、毎日続けやすいから。", "日本語の勉強を早くやめたいから。", "時間がある日だけ勉強したいから。"), correctIndex = 1,
            explanation = "정답: 짧아서 시작하기 쉽고 매일 계속하기 쉬워서. 이전에는 하루 한 시간을 계획했지만 피곤한 날에는 전혀 공부하지 않았습니다. 지금은 10분만이라도 매일 공부하고, 시간이 있으면 더 합니다. 핵심은 긴 시간을 정하는 것보다 매일 계속하는 것이므로 매일 한 시간, 공부를 그만두기, 시간이 있는 날만이라는 보기는 맞지 않습니다.",
        ),
    )

    /** 검수된 초기 묶음. 확장 시 기존 ID를 유지하고 새 고유 ID로 문항을 추가합니다. */
    const val contentVersion = 1

    /** 오늘 푼 문제는 복원하고, 이전 일일 학습 문항을 새 문제로 다시 내지 않습니다. */
    fun daily(date: LocalDate, attempts: List<Attempt> = emptyList()): List<Question> {
        val dailyAttempts = attempts.filterNot { it.review }
        val seen = dailyAttempts.map { it.questionId }.toSet()
        val today = dailyAttempts.filter { it.date == date.toString() }.map { it.questionId }.toSet()
        fun select(kind: String, limit: Int): List<Question> {
            val pool = all.filter { it.kind == kind }
            if (pool.isEmpty()) return emptyList()
            val start = Math.floorMod(date.toEpochDay(), pool.size.toLong()).toInt()
            val rotated = pool.indices.map { pool[(start + it) % pool.size] }
            val restored = rotated.filter { it.id in today }
            val unseen = rotated.filter { it.id !in seen }
            return restored + unseen.take((limit - restored.size).coerceAtLeast(0))
        }
        return select("grammar", 3) + select("reading", 1)
    }
}
