//
//  DBModel.h
//  DreamBox-iOS
//
//  Created by didi on 2020/5/7.
//  Copyright © 2020 didi. All rights reserved.
//

@class DBYogaModel;
@class DBYogaRenderModel;
@class DBReferenceModel;
@class DBViewModelYoga;

typedef NS_ENUM(NSInteger, DBTreeModelLayoutType) {
    DBTreeModelLayoutTypeReference, //相对布局
    DBTreeModelLayoutTypeYoga //弹性布局
};


#pragma mark - viewModel
@interface DBViewModel : NSObject
@property (nonatomic ,copy) NSString *_type;//string
@property (nonatomic ,copy) NSString *type;//string
@property (nonatomic ,copy) NSString *modelID;//string

@property (nonatomic ,copy) NSString *backgroundUrl;//url
@property (nonatomic ,copy) NSString *backgroundColor;//#000000
@property (nonatomic ,copy) NSString *visibleOn;//默认visible，只能接bool，如指定则只能等到true再展示
@property (nonatomic ,copy) NSString *changeOn; //指对哪些数据敏感，当相关数据发生变化时，当前DreamBox视图整体会刷新，属性内容为数据的key，可通过|连接多个数值
@property (nonatomic ,copy) NSString *shape;
@property (nonatomic ,copy) NSString *radius;
@property (nonatomic ,copy) NSString *borderWidth;
@property (nonatomic ,copy) NSString *borderColor;
@property (nonatomic ,copy) NSString *gradientColor;
@property (nonatomic ,copy) NSString *gradientOrientation;
@property (nonatomic ,copy) NSString *scroll;
@property (nonatomic ,copy) NSString *userInteractionEnabled;
@property (nonatomic ,copy) NSString *radiusLT;
@property (nonatomic ,copy) NSString *radiusRT;
@property (nonatomic ,copy) NSString *radiusLB;
@property (nonatomic ,copy) NSString *radiusRB;
//回调节点
@property (nonatomic, strong) NSArray *callbacks;
//触发节点
@property (nonatomic ,strong) NSDictionary *onClick;
@property (nonatomic ,strong) NSDictionary *onVisible;
@property (nonatomic ,strong) NSDictionary *onInvisible;
//子节点
@property (nonatomic,strong) NSArray *children;

//布局节点
@property (nonatomic,strong) DBReferenceModel *referenceLayout;
@property (nonatomic,strong) DBYogaModel *yogaLayout;

+ (DBViewModel *)modelWithDict:(NSDictionary *)dict;
@end

#pragma mark - treeModel
@class DBYogaRenderModel;


@interface DBTreeModel : DBViewModel

@property (nonatomic,strong) NSMutableDictionary *meta;
@property (nonatomic,assign) BOOL dismissOn;
@property (nonatomic,copy) NSString *displayType;
@property (nonatomic,strong) NSDictionary *actionAlias;
@property (nonatomic,strong) NSDictionary *onEvent;
@property (nonatomic,copy) NSString *isSubTree;
@property (nonatomic, copy) NSString *width;
@property (nonatomic, copy) NSString *height;

@end

@interface DBTreeModelReference : DBTreeModel

@property (nonatomic,strong) NSArray *render;
+ (DBTreeModelReference *)modelWithDict:(NSDictionary *)dict;

@end



@interface DBTreeModelYoga : DBTreeModel

@property (nonatomic,strong) DBYogaRenderModel *render;
+ (DBTreeModelYoga *)modelWithDict:(NSDictionary *)dict;

@end



#pragma mark - item view Model
@interface DBTextModel : DBViewModel

@property (nonatomic,copy) NSString *src;
@property (nonatomic,copy) NSString *size;
@property (nonatomic,copy) NSString *color;
@property (nonatomic,copy) NSString *style;
@property (nonatomic,copy) NSString *gravity;
@property (nonatomic,copy) NSString *maxLines;
@property (nonatomic,copy) NSString *ellipsize;
@property (nonatomic,copy) NSString *minWidth;
@property (nonatomic,copy) NSString *maxWidth;
@property (nonatomic,copy) NSString *minHeight;
@property (nonatomic,copy) NSString *maxHeight;

@end

@interface DBProgressModel : DBViewModel

@property (nonatomic,copy) NSString *value;
@property (nonatomic,copy) NSString *barBg;
@property (nonatomic,copy) NSString *barFg;
@property (nonatomic,copy) NSString *barBgColor;
@property (nonatomic,copy) NSString *barFgColor;
@property (nonatomic,copy) NSString *patchType;
@end


@interface DBlistModel : DBViewModel

@property (nonatomic,copy) NSString * src;
@property (nonatomic,copy) NSString * pullRefresh;
@property (nonatomic,copy) NSString *loadMore;
@property (nonatomic,copy) NSString * pageIndex;
@property (nonatomic,copy) NSString * orientation;
@property (nonatomic,copy) NSDictionary *onMore;
@property (nonatomic,copy) NSDictionary *onPull;
@property (nonatomic,copy) NSArray *vh;
@property (nonatomic,copy) NSArray *header;
@property (nonatomic,copy) NSArray *footer;

@end

@interface DBlistModelV2 : DBViewModel

@property (nonatomic,copy) NSString * src;
@property (nonatomic,copy) NSString * pullRefresh;
@property (nonatomic,copy) NSString *loadMore;
@property (nonatomic,copy) NSString * pageIndex;
@property (nonatomic,copy) NSString * orientation;
@property (nonatomic,copy) NSDictionary *onMore;
@property (nonatomic,copy) NSDictionary *onPull;
@property (nonatomic,copy) NSDictionary *vh;
@property (nonatomic,copy) NSDictionary *header;
@property (nonatomic,copy) NSDictionary *footer;

@end


@interface DBImageModel : DBViewModel

@property (nonatomic,copy) NSString *src;
@property (nonatomic,copy) NSString *scaleType;
@property (nonatomic,copy) NSString *srcType;

@end

@interface DBLoadingModel : DBViewModel

@property (nonatomic, copy) NSString *style;

@end

@interface DBFlowModel : DBViewModel

@property (nonatomic,copy) NSString *src;
@property (nonatomic,copy) NSString *hSpace;
@property (nonatomic,copy) NSString *vSpace;

@end


