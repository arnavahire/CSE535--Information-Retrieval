import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.*;

import twitter4j.HashtagEntity;
import twitter4j.JSONArray;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;


public class ArnavSelf 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		//set configuration

		ConfigurationBuilder cb=new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("F86tI72GH3lqsDQ3ghqN3Ybjk")
		.setOAuthConsumerSecret("ZHNitYaM0lqQ7dmOXK05uusevB15YXdJhr2SOHGRKYAAOSIlAu")
        .setOAuthAccessToken("173808583-cwfucuRj4pz2CqHhbBrTI8TyPbZDAYjLUAr7oYEi")
        .setOAuthAccessTokenSecret("0PGldRlixu1iyAgqP51lsyll7PgtWgjkDxoEtxVDCWY59");
		cb.setJSONStoreEnabled(true);
		
		
		//create twitter object
		
		TwitterFactory tf=new TwitterFactory(cb.build());
		Twitter twitterObj=tf.getInstance();
		 final String[] masterEmoticonList ={":-)",":)",":D",":o)",":]",":3",":c)",":>","=]","8)","=)","='(",":}",":^)",":っ)",":-D","8-D","8D","x-D","xD","X-D","XD","=-D","=D","=-3",
	            "=3","B^D",":-))",">:[",":-(",":(",":-c",":c",":-<",":っC",":<",":-[",":[",":{",";(",":-||",":@",">:(",":'-(",":'(",":'-)",":')","D:<","D:","D8","D;","D=","DX","v.v","D-':",
	            ">:O",":-O",":O",":-o",":o","8-","O_O","o-o","O_o","o_O","o_o","O-O",":*",":^*",";-)",";)","*-)","*)",";-]",";]",";D",";^)",">:P",":-P",":P","X-P","x-p","xp","XP",":-p",":p",
	            "=p",":-Þ",":Þ",":þ",":-þ",":-b",":b","d:",">:\\",">:/",":-/",":-.",":\\","=/","=\\",":L","=L",":S",">.<",":|",":-|",":$",":-X",":X",":-#",":#","O:-)",":-3",":3",":-)"
	           ,";^)",">:)",">;)",">:-)","}:-)","}:)","3:-)","3:)","|;-)","|-O",":-J","<3","</3","T_T","@-)","B-)",
	            ":-)",";-)",":-(",":->",">:->",">;->","%-)","#-)",":-I","(-:",":-O",":-o",":*)","8-)",":'-(",":'-)","(:-)",":-@",":-#",":-0",":-S",":-X","8-O","~~:-(",":-&",":-$",
	            "|-I","|-o","|-O","-:-)","-:-(","=:-)","=:-(","8:-)",":-)-8",":-)8<","':-)",",:-)",":-Q",":-?",":-{",":-{)","{:-)","}:-(","B-)",":-{}","::-)","[:-)","[:]","[]","]:->",")8-)","+-:-)",
	            ";^)","!-(","#:-)","$-)","%-\\","%-6","&-|","&-e","&:-)","(-)","(-:|:-)","(:+)","(:-(","(:-D","):-(","):-)",")O-)","*<:-)","*<:-)>","*<|:-)","+:-)","+<:-|",",-)",",-}",".-)",":#)",":'(",":(",
	            ":*",":,(",":-!",":-'|",":-(*)",":-(=)",":-)'",":-)))",":-)-o",":-)~",":-*",":-----}",":--8----",":-/",":-:",":-<",":-[",":-\\",":-]",":-`",":-`|",":-|",":-|-----=",":-|:-|",":-|><",":-}",":-}X",
	            ":-~)",":-3",":-6",":-7",":-8(",":-9",":-a",":-c",":-C",":-d",":-E",":-F",":-o>-[]",":-r",":-T",":-t",":-TM",":-V",":-w",":-y",":/\\)",":=",":=)",":=|",":>",":@",":[",":\\/",":]",":^)",":^D",":_)",
	            ":{",":}",":~(~~",":~-(",":~i",":8)",":C",":I",":O",":Q",":v)","<:-|","<:-I","<<<<:-)","<|-)","<|-(","<||:-)","<|||:|||)","=|:-)=","><*:oDX","><:>==",">>:-)",">>:-<<",">>>>(:-)","?-(","@:-)",
	            "@:-}","@:i","@=","^o","{@:)}","|-(","|-)","|-D","|:-O","|^o","|~(","|I","}:^#)","~:o","~~:-[","0-)","3:-o","3:[","8-#","8:]","8<:-)","b:-)","B:-)","C=:-)","d:-)","d8=","E-:-)","E-:-I","K:P","O-)",
	            "o-<:-{{{","O|-)","P-(","P-)","R-)","X-(","X:-)" };
	     final String[] masterKaomojiList={"▼=ΦωΦ=▼","(ó㉨ò)ﾉ♡","(-̩__-̩)","(❁´◡`❁)","￢o(￣-￣ﾒ)","(￣(∵エ∵)￣)","✖‿✖","ᕙ(⇀‸↼‶)ᕗ","(°ヘ°)","ヾ(*￣O￣)ツ","(◐‿◑)﻿","(✿◠‿◠)","(″･ิ_･ิ)っ","ᶘ ᵒᴥᵒᶅ","(づ￣ ³￣)づ","(☄ฺ◣д◢)☄ฺ","・㉨・","(=ＴェＴ=)","（=´∇｀=","(／(ｴ)＼)","⊙﹏⊙","ミ●﹏☉ミ","＿〆(。。)","(-’๏_๏’-)","<(￣︶￣)>","๏[-ิ_•ิ]๏","ヾ(´〇｀)ﾉ","(╯ಊ╰)","(╯_╰)","Σ(￣。￣ノ)","（／_＼）","(╥_╥)","(=^_^=)","ლ(╹◡╹ლ)","(。_＋)","(⋋▂⋌)","( • ̀ω•́ )","(•ิ_•ิ)","ʕ •́؈•̀ ₎","(✪◡✪)","‾̴̴͡͡ ▿ ‾̴̴͡͡","(✘o✘)","(〃･ิ‿･ิ)ゞ","(•̥̀ ̫ •̥́)","(≧‿≦)","(〃ω〃)","(｡◕ˇ‿ˇ◕｡)","ต(≋°ัω°ั≋)ต","ლ(❤ʚ❤ლ)","(\\___(\\ ☂","ʕ•̫͡•ʔ❤ʕ•̫͡•ʔ","ต(≋°ัω°ั≋)ต","ﾍ(+＿+ﾍ)","／人◕‿‿◕人＼","( ’◉◞౪◟◉`)","(ﾐ◎-◎)ﾉ","／人◕ ‿‿ ◕人＼","ლ(╹◡╹ლ)","╭( •̀؁•́ )╮","(㇏(•̀ᵥᵥ•́)ノ)","(ﾉ●◉▽◉●)ﾉ","ლ(●ʘ‿ʘ● ლ)","( ’◉◞౪◟◉`)","/＼ ^._.^ ／\\","ლ(◉◞౪◟◉ )ლ　","ლ(･ิω･ิლ)","꒰ू ऀ•̥́ꈊ͒ੁ•ૅू॰˳ऀ꒱ ͟͟͞ ̊ ̥ ̥","ლ(⁰⊖⁰ლ)","ლ(╹ε╹ლ)","(๑• .̫ •๑)","ʕ♡˙ᴥ˙♡ʔ","ʕ≧ᴥ≦ʔ","ʕ´•ᴥ•`ʔ","ʕᴥ• ʔ☝","(⁀ᗢ⁀)","(ŏ﹏ŏ)","(｡•ˇ‸ˇ•｡)","(✧◑็அ◑็✧)❤","(ó㉨ò)ﾉ♡","凸(━へ━)","凸(｀0´)凸","(･_･ ) ( ･_･)","(━┳━ _ ━┳━)","[^._.^]ﾉ彡","凸(｀⌒´メ)凸","(•.•ิ)","ᕦ(ò_óˇ)ᕤ","(ノಠ益ಠ)ノ","(〠_〠)","ლ(´ڡ`ლ)","(=^・ェ・^=)","⁝⁞⁝⁞ʕु•̫͡•ʔु☂⁝⁞⁝⁝","(｀ ・ ω ・ ‘)","( -_-)凸","(づ｡◕‿‿◕｡)づ","(づ￣ ³￣)づ","(◣_◢)","(●♡∀♡)","༼ ༎ຶ ෴ ༎ຶ༽","༼´༎ຶ ۝ ༎ຶ༽","(•ˋ _ ˊ•)","⊂(￣(ｴ)￣)⊃","(◕㉨◕✿)","( ❝̆ ·̫̮ ❝̆ )✧","(◡‿◡✿)","(✧≖‿ゝ≖)","(゜。゜)","(*｀へ´*)","(◕‿◕✿)","（▼へ▼メ）","ヾ(-_- )ゞ","ᕙ(⇀‸↼‶)ᕗ","(º_º)","(⊙_◎)","(￣(エ)￣)","(✖╭╮✖)","＜(。_。)＞","m(._.)m","(=ＴェＴ=)","(●￣(ｴ)￣●)","༼´◓ɷ◔`༽","(✪ ㉨ ✪)","( ´ ▽ ` )ﾉ","~(=^‥^)ノ","(￣▼￣)","(=^･ｪ･^=)","༼ꉺ.̫ꉺ༽","༼ꉺ✺ꉺ༽","༼ƪɷ∫༽","٩◔̯◔۶","༼*☁ɷ☁*༽","༼ꉺ✪ꉺ༽","༼ꉺεꉺ༽","(ಥ﹏ಥ)","༼•̃͡ ɷ•̃͡༽","༼๑ɷ๑༽","ミ(° °)ミ","(´ε｀ )♡","ʕ•̀ω•́ʔ✧","(｡♥‿♥｡)","༼ꉺ.̫ꉺ༽","༼☉ɷ⊙༽","(•ิ_•ิ)","༼•̀ɷ•́༽","（＾ｖ＾）","●～*","༼=ꉺ✺ꉺ=༽","(^-人-^)","(•ˋ _ ˊ•)","( ^-^)_旦””","٩꒰ ˘ ³˘꒱۶~♡","(^._.^)ﾉ","ლ(╹◡╹ლ)","༼இɷஇ༽","( ༎ຶ ۝ ༎ຶ )","ヽ(*⌒∇⌒*)ﾉ","༼❁ɷ❁༽",
	                    "（*＾ワ＾*）","（*＾＾*)","ヾ(＠^∇^＠)ノ","o((*^▽^*))o","ヾ(＠⌒ー⌒＠)ノ","(＝⌒▽⌒＝)","(≡^∇^≡)","o(〃＾▽＾〃)o","(。・ω・。)","ヾ(＾-＾)ノ","(❁´◡`❁)","˙˚ʚ(´◡`)ɞ˚˙","(≧∇≦)/","☆*: .｡. o(≧▽≦)o .｡.:*☆","(((o(*ﾟ▽ﾟ*)o)))","ヽ(*⌒∇⌒*)ﾉ","ヽ(＾Д＾)ﾉ"," o(≧∇≦o)","(((＼（＠v＠）／)))","(((o(*ﾟ▽ﾟ*)o)))","✖‿✖","(ﾉ´ヮ´)ﾉ*: ･ﾟ","✧ ─=≡Σ((( つ•̀ω•́)つ","( ✧Д✧) YES!!","(๑˃̵　ᴗ　˂̵)و","(*°∀°)=3","(●♡∀♡)","(♥_♥)","≧﹏≦","（｡>‿‿<｡ ）","ﾄﾞｷﾄﾞｷ(ﾟ∀ﾟ*)(*ﾟ∀ﾟ)ﾄﾞｷﾄﾞｷ","┣¨ｷ(〃ﾟ3ﾟ〃)┣¨ｷ","(^^)ｂ","(＾＾)ｂ OK!","（´∀`）ｂ","( ´ ▽ ` )b","(b^_^)b","b(~_^)d","(b ~_^)b d(^_^ d)","(・ω・)b","(･ω･)bｸﾞｯ","((o･д･)bｸﾞｯｼﾞｮﾌﾞ♪","(ﾟ∇^d) ｸﾞｯ!!","(〃 ω 〃)","(/ω＼)","(^///^)","（*/∇＼*）ｷｬ","(^_-)"

	            };
		int loop=0;
		try
		{
			int i=0;
			String Fin="";
			Query query=new Query("USOpenFinal");
			String lang="en";
			query.setLang(lang);
			new File("Arnav Json Directory").mkdir();
			File jsonfile=new File("S13.json");
			FileWriter writer=new FileWriter(jsonfile);
			List<TweetToJson> Tweetlist=new ArrayList<TweetToJson>();
			
			while(loop<50)
			{
				query.setCount(100);
				
				QueryResult qresult=twitterObj.search(query);
				
				Long LowestStatusID=Long.MAX_VALUE;
		    
				for(Status status:qresult.getTweets())
				{
					Fin=status.getText();
					
					boolean retweet=status.isRetweet();
					String emo="";
					int count1=0;
					for(int j=0;j<masterEmoticonList.length;j++)
					{
						if(status.getText().contains(masterEmoticonList[j]))
						{
							emo=emo+masterEmoticonList[j]+" ";
							
						}
					}
					int count2=0;
					for(int j=0;j<masterKaomojiList.length;j++)
					{
						if(status.getText().contains(masterKaomojiList[j]))
						{
							emo=emo+masterKaomojiList[j]+" ";
						}
					}
					
				/*	List<Emoji> emmo=(List<Emoji>) EmojiManager.getAll();
				
					for(int j=0;j<emmo.size();j++)
					{
						if(status.getText().contains((CharSequence) emmo.get(j)))
						{
							emo=emo+emmo.get(j)+"";
						}
					}*/
					
					for(Emoji e:EmojiManager.getAll())
					{
						if(status.getText().contains(e.getUnicode()))
						{
							emo=emo+e.getUnicode()+" ";
						}
					}
					
					System.out.println(emo);
					
					String RawJson=TwitterObjectFactory.getRawJSON(status);
					
					TweetToJson TtoJ=new TweetToJson();
					
					TtoJ.setScreenName(status.getUser().getScreenName()); //get screen_name
					
					TtoJ.setLang(lang);             //get user language
			   
					TtoJ.setemoticon(emo);  //get emoticons
					
                    //Topic
		    		String Topic="Entertainment";
		    		TtoJ.setTopic(Topic);
		    		
					Date d1=new Date();
					d1=status.getCreatedAt();
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'hh:'00':'00Z'");
					
					String SDate=sdf.format(d1);
					
					TtoJ.setCreated_at(SDate);                  //get date at which tweet was created
		    	
					TtoJ.setTweet_text(status.getText());   //set Tweet text field
		    	
					//set text_xx filed in which text in that lang. will be stored
					
					//IF Language is English
					
					if(lang.equals("en"))
					{
						int hashcount;
						String hash="";
						String s="";
						String aa="";
						
						//For removing hashtags from tweet text in tweet_xx filed and display anly pure text
						
						HashtagEntity[] hArray=status.getHashtagEntities();
						for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
						{
							hash=hash+hArray[hashcount].getText();
							s="#";
							aa=s+hArray[hashcount].getText();
							System.out.println(aa);
						    Fin=Fin.replace(aa,"");
						   // aa=null;
						}		
						
						//For removing urls
						
						int urlcount;
						String url1="";
						URLEntity[] uArray=status.getURLEntities();
						
						for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
						{
							url1=url1+uArray[urlcount].getText();
							aa=uArray[urlcount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						//For removing mentions
						
						int mentioncount;
						String mention="";
						UserMentionEntity[] mArray=status.getUserMentionEntities();
			    	
						for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
						{
							mention=mention+mArray[mentioncount].getText();
							s="@";
							aa=s+mArray[mentioncount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						String po = "RT :";
						
						if(Fin.contains(po))
							Fin=Fin.replace(po, "");
							
						
						TtoJ.setText_en(Fin);
					//	TtoJ.setText_en(status.getText());
					}
					else
					{
						TtoJ.setText_en("");
					}
		    	
					if(lang.equals("ru"))
					{
						int hashcount;
						String hash="";
						String s="";
						String aa="";
						
						//For removing hashtags from tweet text in tweet_xx filed and display anly pure text
						
						HashtagEntity[] hArray=status.getHashtagEntities();
						for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
						{
							hash=hash+hArray[hashcount].getText();
							s="#";
							aa=s+hArray[hashcount].getText();
							System.out.println(aa);
						    Fin=Fin.replace(aa,"");
						   // aa=null;
						}		
						
						//For removing urls
						
						int urlcount;
						String url1="";
						URLEntity[] uArray=status.getURLEntities();
						
						for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
						{
							url1=url1+uArray[urlcount].getText();
							aa=uArray[urlcount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						//For removing mentions
						
						int mentioncount;
						String mention="";
						UserMentionEntity[] mArray=status.getUserMentionEntities();
			    	
						for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
						{
							mention=mention+mArray[mentioncount].getText();
							s="@";
							aa=s+mArray[mentioncount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						String po = "RT :";
						
						if(Fin.contains(po))
							Fin=Fin.replace(po, "");
							
						
						TtoJ.setText_ru(Fin);
					}
					else
					{
						TtoJ.setText_ru("");
					}
		    	
					if(lang.equals("tr"))
					{
						int hashcount;
						String hash="";
						String s="";
						String aa="";
						
						//For removing hashtags from tweet text in tweet_xx filed and display anly pure text
						
						HashtagEntity[] hArray=status.getHashtagEntities();
						for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
						{
							hash=hash+hArray[hashcount].getText();
							s="#";
							aa=s+hArray[hashcount].getText();
							System.out.println(aa);
						    Fin=Fin.replace(aa,"");
						   // aa=null;
						}		
						
						//For removing urls
						
						int urlcount;
						String url1="";
						URLEntity[] uArray=status.getURLEntities();
						
						for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
						{
							url1=url1+uArray[urlcount].getText();
							aa=uArray[urlcount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						//For removing mentions
						
						int mentioncount;
						String mention="";
						UserMentionEntity[] mArray=status.getUserMentionEntities();
			    	
						for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
						{
							mention=mention+mArray[mentioncount].getText();
							s="@";
							aa=s+mArray[mentioncount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						String po = "RT :";
						
						if(Fin.contains(po))
							Fin=Fin.replace(po, "");
							
						
						TtoJ.setText_tr(Fin);
					}
					else
					{
						TtoJ.setText_tr("");
					}
					
					if(lang.equals("es"))
					{
						int hashcount;
						String hash="";
						String s="";
						String aa="";
						
						//For removing hashtags from tweet text in tweet_xx filed and display anly pure text
						
						HashtagEntity[] hArray=status.getHashtagEntities();
						for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
						{
							hash=hash+hArray[hashcount].getText();
							s="#";
							aa=s+hArray[hashcount].getText();
							System.out.println(aa);
						    Fin=Fin.replace(aa,"");
						   // aa=null;
						}		
						
						//For removing urls
						
						int urlcount;
						String url1="";
						URLEntity[] uArray=status.getURLEntities();
						
						for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
						{
							url1=url1+uArray[urlcount].getText();
							aa=uArray[urlcount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						//For removing mentions
						
						int mentioncount;
						String mention="";
						UserMentionEntity[] mArray=status.getUserMentionEntities();
			    	
						for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
						{
							mention=mention+mArray[mentioncount].getText();
							s="@";
							aa=s+mArray[mentioncount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						String po = "RT :";
						
						if(Fin.contains(po))
							Fin=Fin.replace(po, "");
							
						
						TtoJ.setText_es(Fin);
					}
					else
					{
						TtoJ.setText_es("");
					}
					
					if(lang.equals("ko"))
					{
						int hashcount;
						String hash="";
						String s="";
						String aa="";
						
						//For removing hashtags from tweet text in tweet_xx filed and display anly pure text
						
						HashtagEntity[] hArray=status.getHashtagEntities();
						for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
						{
							hash=hash+hArray[hashcount].getText();
							s="#";
							aa=s+hArray[hashcount].getText();
							System.out.println(aa);
						    Fin=Fin.replace(aa,"");
						   // aa=null;
						}		
						
						//For removing urls
						
						int urlcount;
						String url1="";
						URLEntity[] uArray=status.getURLEntities();
						
						for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
						{
							url1=url1+uArray[urlcount].getText();
							aa=uArray[urlcount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						//For removing mentions
						
						int mentioncount;
						String mention="";
						UserMentionEntity[] mArray=status.getUserMentionEntities();
			    	
						for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
						{
							mention=mention+mArray[mentioncount].getText();
							s="@";
							aa=s+mArray[mentioncount].getText();
							Fin=Fin.replace(aa,"");
						}
						
						String po = "RT :";
						
						if(Fin.contains(po))
							Fin=Fin.replace(po, "");
							
						
						TtoJ.setText_ko(Fin);
					}	
					else
					{
						TtoJ.setText_ko("");
					}
					//Retweets
					
					TtoJ.setRetweet(status.isRetweet());
					
					//Hashtags
		
					int hashcount;
					String hash="";
					HashtagEntity[] hArray=status.getHashtagEntities();
					for(hashcount=0;hashcount<status.getHashtagEntities().length;hashcount++)
					{
						hash=hash+hArray[hashcount].getText();
						if(hashcount<status.getHashtagEntities().length-1)
						{
							hash=hash+" ";
						}
					}	
					TtoJ.appendTwitter_hashtag(hash);
					
					//URLs
					
					int urlcount;
					String url1="";
					URLEntity[] uArray=status.getURLEntities();
					
					for(urlcount=0;urlcount<status.getURLEntities().length;urlcount++)
					{
						url1=url1+uArray[urlcount].getText();
						if(urlcount<status.getHashtagEntities().length-1)
						{
							url1=url1+" ";
						}
					}	
					TtoJ.appendtwitter_url(url1);
					
					//Geolocation--- NEEDS TO BE EDITEDDDDDDDDDDDDD!!!!!!!!!!!!!!!!!
		    	
					if(status.getGeoLocation()!=null)
					{
						TtoJ.setTweet_loc((status.getGeoLocation().getLatitude()+","+status.getGeoLocation().getLongitude()));
					}
					else
					{
						TtoJ.setTweet_loc("0,0");
					}
		    	
					//UserMentions
		    	
					int mentioncount;
					String mention="";
					UserMentionEntity[] mArray=status.getUserMentionEntities();
		    	
					for(mentioncount=0;mentioncount<status.getUserMentionEntities().length;mentioncount++)
					{
						mention=mention+mArray[mentioncount].getText();
						if(mentioncount<status.getUserMentionEntities().length-1)
						{
							mention=mention+" ";
						}
					}
					TtoJ.setMentions(mention);
		    		
					LowestStatusID=Math.min(LowestStatusID,status.getId());
					
					Tweetlist.add(TtoJ);
					i++;
				} // For loop ends
		    		query.setMaxId(LowestStatusID);
				loop++;
				} //while loop ends
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
		    System.out.println(gson.toJson(Tweetlist));
		    writer.write(gson.toJson(Tweetlist));
		    writer.flush();
		    writer.close(); 
		    System.out.println(i);
			}  //try ends
		catch(Exception e)
		{
			System.out.println("There seems to be an exception"+e.getMessage());
			e.printStackTrace();
		}
	}
}